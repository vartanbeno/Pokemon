package app;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.rdg.CardRDG;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.hand.rdg.HandRDG;

@WebServlet("/DrawCard")
public class DrawCard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
       
    public DrawCard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
				long player = getUserId(request);
				long gameId = Long.parseLong(request.getParameter("game"));
				
				GameRDG game = GameRDG.findById(gameId);
				
				// TODO error handling, if game is null
				
				List<HandRDG> hand = HandRDG.findByGameAndPlayer(game.getId(), player);
				int handSize = hand.size();
				
				System.out.println("You have " + handSize + " cards in your hand.");
				
				DeckRDG deck = DeckRDG.findByPlayer(player);
				List<CardRDG> cards = CardRDG.findByDeck(deck.getId());
				
				for (int i = 0; i < handSize; i++) {
					cards.remove(0);
				}
				
				// TODO error handling, if no more cards left in deck
				
				HandRDG drawCard = new HandRDG(
						HandRDG.getMaxId(),
						game.getId(),
						player,
						deck.getId(),
						cards.remove(0).getId()
				);
				drawCard.insert();
				
				hand.add(drawCard);
				
				System.out.println("You now have " + hand.size() + " cards in your hand.");
				System.out.println(cards.size() + " left in the deck.");
				
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

}
