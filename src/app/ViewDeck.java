package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.CardHelper;
import dom.model.card.rdg.CardRDG;
import dom.model.deck.DeckHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ViewDeck")
public class ViewDeck extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOGIN_FAIL_MESSAGE = "You must be logged in to view your deck.";
	private static final String NO_DECK = "You do not have a deck. Upload one!";
	
       
    public ViewDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			if (loggedIn(request)) {
				
				long player = getUserId(request);
				DeckRDG deck = DeckRDG.findByPlayer(player);
				
				if (deck == null) {
					failure(request, response, NO_DECK);
				}
				else {
					
					UserRDG ownerOfDeck = UserRDG.findById(deck.getId());
					UserHelper userHelper = new UserHelper(
							ownerOfDeck.getId(),
							ownerOfDeck.getVersion(),
							ownerOfDeck.getUsername(),
							""
					);
					
					DeckHelper deckHelper = new DeckHelper(deck.getId(), userHelper);
					
					List<CardRDG> cards = CardRDG.findByDeck(deck.getId());
					List<CardHelper> cardHelpers = new ArrayList<CardHelper>();
					CardHelper cardHelper = null;
					
					for (CardRDG card : cards) {
						
						cardHelper = new CardHelper(deckHelper, card.getType(), card.getName());
						cardHelpers.add(cardHelper);
						
					}
					
					request.setAttribute("cards", cardHelpers);
					request.getRequestDispatcher(Global.DECK).forward(request, response);
					
				}
				
			}
			else {
				failure(request, response, LOGIN_FAIL_MESSAGE);
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
