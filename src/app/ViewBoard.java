package app;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.deck.DeckHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.GameHelper;
import dom.model.game.rdg.GameRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ViewBoard")
public class ViewBoard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view a game board.";
	private static final String WRONG_FORMAT = "You must specify a game ID in the correct format (an integer).";
	private static final String INVALID_GAME = "The game you specified does not exist.";
	private static final String NOT_YOUR_GAME = "You are not part of this game.";
	
    public ViewBoard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
				Long gameId = null;
				
				try {
					gameId = Long.parseLong(request.getParameter("game"));
				}
				catch (NumberFormatException e) {
					failure(request, response, WRONG_FORMAT);
					return;
				}
				
				GameRDG gameRDG = GameRDG.findById(gameId);
				
				if (gameRDG != null) {
					
					long userId = getUserId(request);
					
					if (userId != gameRDG.getChallenger() && userId != gameRDG.getChallengee()) {
						failure(request, response, NOT_YOUR_GAME);
						return;
					}
					
					UserRDG challengerRDG = UserRDG.findById(gameRDG.getChallenger());
					UserRDG challengeeRDG = UserRDG.findById(gameRDG.getChallengee());
					
					DeckRDG challengerDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallenger());
					DeckRDG challengeeDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallengee());
					
					UserHelper challenger = new UserHelper(
							challengerRDG.getId(),
							challengerRDG.getVersion(),
							challengerRDG.getUsername(),
							""
					);
					
					UserHelper challengee = new UserHelper(
							challengeeRDG.getId(),
							challengeeRDG.getVersion(),
							challengeeRDG.getUsername(),
							""
					);
					
					DeckHelper challengerDeck = new DeckHelper(
							challengerDeckRDG.getId(),
							challenger
					);
					
					DeckHelper challengeeDeck = new DeckHelper(
							challengeeDeckRDG.getId(),
							challengee
					);
					
					GameHelper game = new GameHelper(
							gameRDG.getId(),
							challenger,
							challengee,
							challengerDeck,
							challengeeDeck
					);
					
					request.setAttribute("game", game);
					request.getRequestDispatcher(Global.VIEW_BOARD).forward(request, response);
					
				}
				else {
					success(request, response, INVALID_GAME);
				}
				
			}
			else {
				failure(request, response, NOT_LOGGED_IN);
			}
			
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
