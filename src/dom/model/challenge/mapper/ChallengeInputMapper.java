package dom.model.challenge.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.challenge.Challenge;
import dom.model.challenge.IChallenge;
import dom.model.challenge.tdg.ChallengeFinder;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class ChallengeInputMapper {
	
	public static List<IChallenge> findAll() throws SQLException {
		
		ResultSet rs = ChallengeFinder.findAll();
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge findById(long id) throws SQLException {
		
		ResultSet rs = ChallengeFinder.findById(id);
		
		Challenge challenge = rs.next() ? buildChallenge(rs) : null;
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
		
		ResultSet rs = ChallengeFinder.findOpenById(id);
		
		Challenge challenge = rs.next() ? buildChallenge(rs) : null;
		rs.close();
		
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
		
		ResultSet rs = ChallengeFinder.findOpenByChallengerAndChallengee(challenger, challengee);
		
		Challenge challenge = rs.next() ? buildChallenge(rs) : null;
		rs.close();
		
		return challenge;
		
	}
	
	public static Challenge buildChallenge(ResultSet rs) throws SQLException {
		
		User challenger = UserInputMapper.findById(rs.getLong("challenger"));
		User challengee = UserInputMapper.findById(rs.getLong("challengee"));
		Deck challengerDeck = DeckInputMapper.findById(rs.getLong("challenger_deck"));
		
		return new Challenge(
				rs.getLong("id"),
				rs.getLong("version"),
				challenger, challengee,
				rs.getInt("status"),
				challengerDeck
			);
		
	}
	
	public static List<IChallenge> buildChallenges(ResultSet rs) throws SQLException {
		
		List<IChallenge> challenges = new ArrayList<IChallenge>();
		
		while (rs.next()) {
			
			Challenge challenge = buildChallenge(rs);
			challenges.add(challenge);
			
		}
		
		rs.close();
		
		return challenges;
		
	}

}
