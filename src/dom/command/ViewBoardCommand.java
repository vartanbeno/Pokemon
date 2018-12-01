package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.game.GameStatus;
import dom.model.game.IGame;
import dom.model.game.IGameBoard;
import dom.model.game.mapper.GameInputMapper;

public class ViewBoardCommand extends AbstractCommand {
	
	private static final String PLAYING = "playing";
	private static final String RETIRED = "retired";
	private static final String WON = "won";
	private static final String LOST = "lost";
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your game board.";
	
	@SetInRequestAttribute
	public IGameBoard gameBoard;
	
	@SetInRequestAttribute
	public String challengerStatus;
	
	@SetInRequestAttribute
	public String challengeeStatus;

	public ViewBoardCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			IGame game = getGame(gameId);
			
			checkIfImPartOfGame(game);
			
			gameBoard = GameInputMapper.buildGameBoard(game);
			
			if (gameBoard.getGame().getStatus() == GameStatus.ongoing.ordinal()) {
				challengerStatus = challengeeStatus = PLAYING;
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengerRetired.ordinal()) {
				challengerStatus = RETIRED;
				challengeeStatus = PLAYING;
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengeeRetired.ordinal()) {
				challengerStatus = PLAYING;
				challengeeStatus = RETIRED;
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengerWon.ordinal()) {
				challengerStatus = WON;
				challengeeStatus = LOST;
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengeeWon.ordinal()) {
				challengerStatus = LOST;
				challengeeStatus = WON;
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
