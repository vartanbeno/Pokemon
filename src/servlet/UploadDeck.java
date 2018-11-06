package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.cardsindeck.rdg.CardsInDeckRDG;
import dom.model.deck.rdg.DeckRDG;

@WebServlet("/UploadDeck")
public class UploadDeck extends PageController {

	private static final long serialVersionUID = 1L;
	
	private static final String LOGIN_FAIL_MESSAGE = "You must be logged in to upload a deck.";
	private static final String DECK_FAIL_MESSAGE = "You already have a deck.";
	private static final String CARDS_FAIL_MESSAGE = "You have %1$d cards in your deck. You must have %2$d.";
	
	private static final String DECK_SUCCESS_MESSAGE = "You have successfully uploaded your deck.";
	
    public UploadDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
				long player = getUserId(request);
				DeckRDG deck = DeckRDG.findByPlayer(player);
				
				if (deck == null) {
					request.setAttribute("numberOfCards", CardsInDeckRDG.getCardsPerDeck());
					request.getRequestDispatcher(Global.CREATE_DECK_FORM).forward(request, response);
				}
				else {
					failure(request, response, DECK_FAIL_MESSAGE);
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
		
		try {
			
			if (loggedIn(request)) {
				
				/**
				 * We delete carriage returns (\r).
				 * We make sure to trim the String, i.e. delete leftmost and rightmost spaces/newlines.
				 * Then, we get the cards by splitting by newline.
				 */
				String cards[] = request.getParameter("deck").replace("\r", "").trim().split("\n");
				System.out.println(Arrays.toString(cards));
				
				if (cards.length != CardsInDeckRDG.getCardsPerDeck()) {
					failure(request, response, String.format(CARDS_FAIL_MESSAGE, cards.length, CardsInDeckRDG.getCardsPerDeck()));
				}
				else {
					
					// TODO
					
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

}
