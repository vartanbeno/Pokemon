package dom.model.challenge.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.challenge.Challenge;
import dom.model.challenge.IChallenge;
import dom.model.challenge.tdg.ChallengeTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserMapper;
import dom.model.user.tdg.UserTDG;

public class ChallengeMapper {
	
	public static List<IChallenge> findAll() throws SQLException {
		
		ResultSet rs = ChallengeTDG.findAll();
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge findById(long id) throws SQLException {
		
		ResultSet rs = ChallengeTDG.findById(id);
		
		Challenge challenge = rs.next() ? buildChallenge(rs) : null;
		rs.close();
		
		return challenge;
		
	}
	
	public static List<IChallenge> findByChallenger(long challenger) throws SQLException {
		
		ResultSet rs = ChallengeTDG.findByChallenger(challenger);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static List<IChallenge> findByChallengee(long challengee) throws SQLException {
		
		ResultSet rs = ChallengeTDG.findByChallengee(challengee);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge findOpenById(long id) throws SQLException {
		
		ResultSet rs = ChallengeTDG.findOpenById(id);
		
		Challenge challenge = rs.next() ? buildChallenge(rs) : null;
		rs.close();
		
		return challenge;
		
	}
	
	public static List<IChallenge> findOpenByChallenger(long challenger) throws SQLException {
		
		ResultSet rs = ChallengeTDG.findOpenByChallenger(challenger);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static List<IChallenge> findOpenByChallengee(long challengee) throws SQLException {
		
		ResultSet rs = ChallengeTDG.findOpenByChallengee(challengee);
		
		List<IChallenge> challenges = buildChallenges(rs);
		rs.close();
		
		return challenges;
		
	}
	
	public static Challenge findOpenByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		
		ResultSet rs = ChallengeTDG.findOpenByChallengerAndChallengee(challenger, challengee);
		
		Challenge challenge = rs.next() ? buildChallenge(rs) : null;
		rs.close();
		
		return challenge;
		
	}
	
	public static void insert(Challenge challenge) throws SQLException {
		ChallengeTDG.insert(challenge.getId(), challenge.getChallenger().getId(), challenge.getChallengee().getId());
	}
	
	public static void update(Challenge challenge) throws SQLException {
		ChallengeTDG.update(challenge.getStatus(), challenge.getId());
	}
	
	public static void delete(Challenge challenge) throws SQLException {
		ChallengeTDG.delete(challenge.getId());
	}
	
	public static Challenge buildChallenge(ResultSet rs) throws SQLException {
		
		ResultSet challengerRS = UserTDG.findById(rs.getLong("challenger"));
		User challenger = challengerRS.next() ? UserMapper.buildUser(challengerRS) : null;
		challengerRS.close();
		
		ResultSet challengeeRS = UserTDG.findById(rs.getLong("challengee"));
		User challengee = challengeeRS.next() ? UserMapper.buildUser(challengeeRS) : null;
		challengeeRS.close();
		
		return new Challenge(
				rs.getLong("id"),
				challenger,
				challengee,
				rs.getInt("status")
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
