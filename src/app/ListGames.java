package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

@WebServlet("/ListGames")
public class ListGames extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view the list of games.";
       
    public ListGames() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			List<GameRDG> gameRDGs = GameRDG.findAll();
			List<GameHelper> games = new ArrayList<GameHelper>();
			
			for (GameRDG gameRDG : gameRDGs) {
				
				UserRDG challengerRDG = UserRDG.findById(gameRDG.getChallenger());
				UserHelper challenger = new UserHelper(
						challengerRDG.getId(),
						challengerRDG.getVersion(),
						challengerRDG.getUsername(),
						""
				);
				
				UserRDG challengeeRDG = UserRDG.findById(gameRDG.getChallengee());
				UserHelper challengee = new UserHelper(
						challengeeRDG.getId(),
						challengeeRDG.getVersion(),
						challengeeRDG.getUsername(),
						""
				);
				
				DeckRDG challengerDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallenger());
				DeckHelper challengerDeck = new DeckHelper(challengerDeckRDG.getId(), challenger);
				
				DeckRDG challengeeDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallengee());
				DeckHelper challengeeDeck = new DeckHelper(challengeeDeckRDG.getId(), challengee);
				
				GameHelper game = new GameHelper(
						gameRDG.getId(),
						challenger,
						challengee,
						challengerDeck,
						challengeeDeck
				);
				
				games.add(game);
				
			}
			
			request.setAttribute("games", games);
			request.getRequestDispatcher(Global.LIST_GAMES).forward(request, response);
			
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
