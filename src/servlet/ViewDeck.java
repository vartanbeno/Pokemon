package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.cardsindeck.rdg.CardsInDeckRDG;
import dom.model.deck.rdg.DeckRDG;

@WebServlet("/ViewDeck")
public class ViewDeck extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOGIN_FAIL_MESSAGE = "You must be logged in to view your deck.";
	private static final String DECK_FAIL_MESSAGE = "You do not have a deck.";
	
       
    public ViewDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			if (loggedIn(request)) {
				
				long player = getUserId(request);
				DeckRDG deck = DeckRDG.findByPlayer(player);
				
				if (deck == null) {
					failure(request, response, DECK_FAIL_MESSAGE);
				}
				else {
					
					List<CardsInDeckRDG> cards = CardsInDeckRDG.findByDeck(deck.getId());
					
					request.setAttribute("cards", cards);
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
