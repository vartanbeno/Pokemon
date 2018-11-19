package dom.model.game.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.game.Game;
import dom.model.game.tdg.GameTDG;

public class GameOutputMapper extends GenericOutputMapper<Long, Game> {
	
	@Override
	public void insert(Game game) throws MapperException {
		try {
			insertStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Game game) throws MapperException {
		try {
			updateStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public void updateVersion(Game game) throws MapperException {
		try {
			updateVersionStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Game game) throws MapperException {
		try {
			deleteStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(Game game) throws SQLException {
		GameTDG.insert(
				game.getId(),
				game.getVersion(),
				game.getChallenger().getId(),
				game.getChallengee().getId(),
				game.getChallengerDeck().getId(),
				game.getChallengeeDeck().getId()
		);
	}
	
	public static void updateStatic(Game game) throws SQLException, LostUpdateException {
		int count = GameTDG.update(game.getId(), game.getVersion(), game.getStatus());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update game with id: %d.", game.getId()));
	}
	
	public static void updateVersionStatic(Game game) throws SQLException, LostUpdateException {
		int count = GameTDG.updateVersion(game.getId(), game.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update game with id: %d.", game.getId()));
	}
	
	public static void deleteStatic(Game game) throws SQLException, LostUpdateException {
		int count = GameTDG.delete(game.getId(), game.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot delete game with id: %d.", game.getId()));
	}

}
