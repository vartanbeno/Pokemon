package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.game.Game;
import dom.model.game.GameStatus;

public class EndTurnCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String NOT_YOUR_GAME = "You are not part of this game.";
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
	
	public EndTurnCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			long userId = getUserId();
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			Game game = getGame(gameId);
			
			if (userId != game.getChallenger().getId() && userId != game.getChallengee().getId())
				throw new CommandException(NOT_YOUR_GAME);
			if (game.getStatus() != GameStatus.ongoing.ordinal()) throw new CommandException(GAME_STOPPED);
			
			this.message = "you are part of this game";
			
			// TODO logic
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
