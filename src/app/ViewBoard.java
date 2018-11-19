package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameStatus;
import dom.model.game.mapper.GameInputMapper;

@WebServlet("/ViewBoard")
public class ViewBoard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view a game board.";
	
    public ViewBoard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			Game game = getGame(request, response);
			if (game == null) return;
			
			GameBoard gameBoard = GameInputMapper.buildGameBoard(game);
			
			String challengerStatus = "";
			String challengeeStatus = "";
			
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
			
			request.setAttribute("challengerStatus", challengerStatus);
			request.setAttribute("challengeeStatus", challengeeStatus);
			request.setAttribute("gameBoard", gameBoard);
			
			request.getRequestDispatcher(Global.VIEW_BOARD).forward(request, response);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);		
	}

}
