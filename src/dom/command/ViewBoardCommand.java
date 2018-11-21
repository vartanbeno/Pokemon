package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.game.Game;
import dom.model.game.GameStatus;
import dom.model.game.IGameBoard;
import dom.model.game.mapper.GameInputMapper;

public class ViewBoardCommand extends AbstractCommand {
	
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
			
			Game game = getGame();
			gameBoard = GameInputMapper.buildGameBoard(game);
			
			if (gameBoard.getGame().getStatus() == GameStatus.ongoing.ordinal()) {
				challengerStatus = challengeeStatus = "playing";
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengerRetired.ordinal()) {
				challengerStatus = "retired";
				challengeeStatus = "won";
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengeeRetired.ordinal()) {
				challengerStatus = "won";
				challengeeStatus = "retired";
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengerWon.ordinal()) {
				challengerStatus = "won";
				challengeeStatus = "lost";
			}
			else if (gameBoard.getGame().getStatus() == GameStatus.challengeeWon.ordinal()) {
				challengerStatus = "lost";
				challengeeStatus = "won";
			}
			
		}
		catch (Exception e) {
			this.message = e.getMessage();
			throw new CommandException(e.getMessage());
		}
		
	}

}
