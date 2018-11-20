package dom.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.game.Game;
import dom.model.game.mapper.GameInputMapper;

public abstract class AbstractCommand extends ValidatorCommand {
	
	/**
	 * Game fail messages.
	 */
	private static final String GAME_ID_FORMAT = "You must specify a game ID in the correct format (a positive integer).";
	private static final String GAME_DOES_NOT_EXIST = "The game you specified does not exist.";
	private static final String NOT_YOUR_GAME = "You are not part of this game.";

	public AbstractCommand(Helper helper) {
		super(helper);
	}

	@Override
	public abstract void process() throws CommandException;
	
	protected long getUserId() {
		return (long) helper.getSessionAttribute("userid");
	}
	
	protected void checkIfLoggedIn(String message) throws CommandException {
		try {
			getUserId();
		}
		catch (NullPointerException e) {
			throw new CommandException(message);
		}
	}
	
	protected Game getGame() throws CommandException, SQLException {
		
		Long gameId = null;
		long userId = getUserId();
		
		try {
			gameId = helper.getLong("game");
		}
		catch (NumberFormatException e) {
			throw new CommandException(GAME_ID_FORMAT);
		}
		
		Game game = GameInputMapper.findById(gameId);
		if (game == null) throw new CommandException(GAME_DOES_NOT_EXIST);
		if (game.getChallenger().getId() != userId && game.getChallengee().getId() != userId) throw new CommandException(NOT_YOUR_GAME);
		
		return game;
		
	}

}
