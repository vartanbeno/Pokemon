package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.uow.UoW;

import dom.model.game.Game;
import dom.model.game.GameFactory;
import dom.model.game.GameStatus;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class RetireCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to retire from a game.";
	private static final String GAME_ALREADY_ENDED = "This game has already ended.";
    
	private static final String RETIRE_SUCCESS = "You have successfully retired from your game against %s.";
	
	@SetInRequestAttribute
	public String message;
	
	public RetireCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			Game game = getGame();
			if (game.getStatus() != GameStatus.ongoing.ordinal()) throw new CommandException(GAME_ALREADY_ENDED);
			
			long userId = getUserId();
			User opponent = null;
			
			if (userId == game.getChallenger().getId()) {
				game.setStatus(GameStatus.challengerRetired.ordinal());
				opponent = UserInputMapper.findById(game.getChallengee().getId());
			}
			else if (userId == game.getChallengee().getId()) {
				game.setStatus(GameStatus.challengeeRetired.ordinal());
				opponent = UserInputMapper.findById(game.getChallenger().getId());
			}
			
			GameFactory.registerDirty(game);
			UoW.getCurrent().commit();
			
			message = String.format(RETIRE_SUCCESS, opponent.getUsername());
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
