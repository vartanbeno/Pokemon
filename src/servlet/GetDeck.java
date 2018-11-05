package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.rdg.CardRDG;
import dom.model.cardsindeck.rdg.CardsInDeckRDG;
import dom.model.deck.rdg.DeckRDG;

@WebServlet("/GetDeck")
public class GetDeck extends PageController {

	private static final long serialVersionUID = 1L;
       
    public GetDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request, response)) {
				
				long player = (long) request.getSession(true).getAttribute("userid");
				
				DeckRDG deckRDG = DeckRDG.findByPlayer(player);
				if (deckRDG == null) {
					request.getRequestDispatcher(Global.CREATE_DECK_FORM).forward(request, response);
				}
				else {
					List<CardsInDeckRDG> cardsInDeckRDGs = CardsInDeckRDG.findByDeck(deckRDG.getId());
					List<CardRDG> deck = new ArrayList<CardRDG>();
					
					for (CardsInDeckRDG cardsInDeckRDG : cardsInDeckRDGs) {
						deck.add(CardRDG.findById(cardsInDeckRDG.getCard()));
					}
					
					request.setAttribute("cards", deck);
					request.getRequestDispatcher(Global.DECK).forward(request, response);
				}
				
			}
			else {
				failure(request, response, "You must be logged in to get a deck.");
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
		
		try {
			
			if (loggedIn(request, response)) {
				
				long player = (long) request.getSession(true).getAttribute("userid");
				DeckRDG deckRDG = DeckRDG.findByPlayer(player);
				
				if (deckRDG == null) {
					deckRDG = new DeckRDG(DeckRDG.getMaxId(), player);
					deckRDG.insert();
					CardsInDeckRDG.createDeck(deckRDG.getId());
				}
				
				doGet(request, response);
				
			}
			else {
				failure(request, response, "You must be logged in to create a deck.");
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
