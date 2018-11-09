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
import dom.model.deck.DeckWithCardsHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ViewDeck")
public class ViewDeck extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your deck.";
	private static final String NO_DECK = "You do not have a deck. Upload one!";
	
       
    public ViewDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			long player = getUserId(request);
			DeckRDG deck = DeckRDG.findByPlayer(player);
			
			if (deck == null) {
				failure(request, response, NO_DECK);
				return;
			}
			
			UserRDG ownerRDG = UserRDG.findById(deck.getPlayer());
			UserHelper owner = new UserHelper(
					ownerRDG.getId(),
					ownerRDG.getVersion(),
					ownerRDG.getUsername(),
					""
			);
			
			DeckHelper deckHelper = new DeckHelper(deck.getId(), owner);
			
			List<CardRDG> cardRDGs = CardRDG.findByDeck(deck.getId());
			List<CardHelper> cards = new ArrayList<CardHelper>();
			
			for (CardRDG cardRDG : cardRDGs) {
				
				CardHelper card = new CardHelper(
						cardRDG.getId(),
						deckHelper,
						cardRDG.getType(),
						cardRDG.getName()
				);
				
				cards.add(card);
				
			}
			
			DeckWithCardsHelper deckWithCardsHelper = new DeckWithCardsHelper(deck.getId(), owner, cards);
			
			request.setAttribute("deck", deckWithCardsHelper);
			request.getRequestDispatcher(Global.VIEW_DECK).forward(request, response);
			
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
