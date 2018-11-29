package dom.model.challenge.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.challenge.Challenge;
import dom.model.challenge.tdg.ChallengeTDG;

public class ChallengeOutputMapper extends GenericOutputMapper<Long, Challenge> {
	
	@Override
	public void insert(Challenge challenge) throws MapperException {
		try {
			ChallengeTDG.insert(
					challenge.getId(),
					challenge.getVersion(),
					challenge.getChallenger().getId(),
					challenge.getChallengee().getId(),
					challenge.getChallengerDeck().getId()
			);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Challenge challenge) throws MapperException {
		try {
			int count = ChallengeTDG.update(challenge.getId(), challenge.getVersion(), challenge.getStatus());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update challenge with id: %d.", challenge.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Challenge challenge) throws MapperException {
		try {
			int count = ChallengeTDG.delete(challenge.getId(), challenge.getVersion());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete challenge with id: %d.", challenge.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

}
