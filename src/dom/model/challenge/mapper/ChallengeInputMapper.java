package dom.model.challenge.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.IChallenge;
import dom.model.challenge.tdg.ChallengeFinder;
import dom.model.deck.DeckProxy;
import dom.model.user.UserProxy;

public class ChallengeInputMapper {
	
	public static List<IChallenge> findAll() throws SQLException {
		
		ResultSet rs = ChallengeFinder.findAll();
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge findById(long id) throws SQLException {
		
		Challenge challenge = getFromIdentityMap(id);
		if (challenge != null) return challenge;
		
		ResultSet rs = ChallengeFinder.findById(id);
		
		challenge = rs.next() ? buildChallenge(rs) : null;
		rs.close();
		
		return challenge;
		
	}
	
	public static List<IChallenge> findByChallenger(long challenger) throws SQLException {
		
		ResultSet rs = ChallengeFinder.findByChallenger(challenger);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static List<IChallenge> findByChallengee(long challengee) throws SQLException {
		
		ResultSet rs = ChallengeFinder.findByChallengee(challengee);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge findOpenById(long id) throws SQLException {
		
		Challenge challenge = null;
		
		ResultSet rs = ChallengeFinder.findOpenById(id);
		
		if (rs.next()) {
			
			long id2 = rs.getLong("id");
			
			challenge = getFromIdentityMap(id2);
			if (challenge == null) challenge = buildChallenge(rs);
			
			rs.close();
			
		}
		
		return challenge;
		
	}
	
	public static List<IChallenge> findOpenByChallenger(long challenger) throws SQLException {
		
		ResultSet rs = ChallengeFinder.findOpenByChallenger(challenger);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static List<IChallenge> findOpenByChallengee(long challengee) throws SQLException {
		
		ResultSet rs = ChallengeFinder.findOpenByChallengee(challengee);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge findOpenByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		
		Challenge challenge = null;
		
		ResultSet rs = ChallengeFinder.findOpenByChallengerAndChallengee(challenger, challengee);
		
		if (rs.next()) {
			
			long id = rs.getLong("id");
			
			challenge = getFromIdentityMap(id);
			if (challenge == null) challenge = buildChallenge(rs);
			
			rs.close();
			
		}
		
		return challenge;
		
	}
	
	public static Challenge buildChallenge(ResultSet rs) throws SQLException {
				
		return ChallengeFactory.createClean(
				rs.getLong("id"),
				rs.getLong("version"),
				new UserProxy(rs.getLong("challenger")),
				new UserProxy(rs.getLong("challengee")),
				rs.getInt("status"),
				new DeckProxy(rs.getLong("challenger_deck"))
			);
		
	}
	
	public static List<IChallenge> buildChallenges(ResultSet rs) throws SQLException {
		
		List<IChallenge> challenges = new ArrayList<IChallenge>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			Challenge challenge = getFromIdentityMap(id);
			
			if (challenge == null) challenge = buildChallenge(rs);
			
			challenges.add(challenge);
			
		}
		
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge getFromIdentityMap(long id) {
		
		Challenge challenge = null;
		
		try {
			challenge = IdentityMap.get(id, Challenge.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return challenge;
		
	}

}
