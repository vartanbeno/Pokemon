package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.game.GameFactory;
import dom.model.game.GameStatus;
import dom.model.game.IGame;
import dom.model.user.IUser;
import dom.model.user.mapper.UserInputMapper;

public class RetireCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to retire from a game.";
    
	private static final String RETIRE_SUCCESS = "You have successfully retired from your game against %s.";
	
	public RetireCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			IGame game = getGame(gameId);
			
			checkIfImPartOfGame(game);
			checkIfGameHasEnded(game);
			
			long userId = getUserId();
			IUser opponent = null;
			
			if (userId == game.getChallenger().getId()) {
				game.setStatus(GameStatus.challengerRetired.ordinal());
				opponent = UserInputMapper.findById(game.getChallengee().getId());
			}
			else if (userId == game.getChallengee().getId()) {
				game.setStatus(GameStatus.challengeeRetired.ordinal());
				opponent = UserInputMapper.findById(game.getChallenger().getId());
			}
			
			GameFactory.registerDirty(
					game.getId(),
					game.getVersion(),
					game.getChallenger(),
					game.getChallengee(),
					game.getChallengerDeck(),
					game.getChallengeeDeck(),
					game.getCurrentTurn(),
					game.getTurn(),
					game.getStatus()
			);
			
			this.message = String.format(RETIRE_SUCCESS, opponent.getUsername());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
