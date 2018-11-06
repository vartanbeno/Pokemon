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
       
    public ListGames() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			List<GameRDG> gameRDGs = GameRDG.findAll();
			
			UserRDG challengerRDG = null;
			UserHelper challenger = null;
			
			UserHelper challengee = null;
			UserRDG challengeeRDG = null;
			
			DeckRDG challengerDeckRDG = null;
			DeckHelper challengerDeck = null;
			
			DeckRDG challengeeDeckRDG = null;
			DeckHelper challengeeDeck = null;
			
			List<GameHelper> games = new ArrayList<GameHelper>();
			GameHelper game = null;
			
			for (GameRDG gameRDG : gameRDGs) {
				
				challengerRDG = UserRDG.findById(gameRDG.getChallenger());
				challenger = new UserHelper(
						challengerRDG.getId(),
						challengerRDG.getVersion(),
						challengerRDG.getUsername(),
						""
				);
				
				challengeeRDG = UserRDG.findById(gameRDG.getChallengee());
				challengee = new UserHelper(
						challengeeRDG.getId(),
						challengeeRDG.getVersion(),
						challengeeRDG.getUsername(),
						""
				);
				
				challengerDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallenger());
				challengerDeck = new DeckHelper(challengerDeckRDG.getId(), challenger);
				
				challengeeDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallengee());
				challengeeDeck = new DeckHelper(challengeeDeckRDG.getId(), challengee);
				
				game = new GameHelper(
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
