package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.rdg.CardRDG;
import dom.model.deck.rdg.DeckRDG;

@WebServlet("/UploadDeck")
public class UploadDeck extends PageController {

	private static final long serialVersionUID = 1L;
	
	private static final String LOGIN_FAIL_MESSAGE = "You must be logged in to upload a deck.";
	private static final String DECK_FAIL_MESSAGE = "You already have a deck.";
	private static final String CARDS_FAIL_MESSAGE = "You have %1$d card(s) in your deck. You must have %2$d.";
	
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
					request.setAttribute("numberOfCards", CardRDG.getCardsPerDeck());
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
				
				long player = getUserId(request);
				
				DeckRDG deck = DeckRDG.findByPlayer(player);
				
				if (deck != null) {
					failure(request, response, DECK_FAIL_MESSAGE);
				}
				else {
					
					/**
					 * We delete carriage returns (\r) by replacing them with an empty String "".
					 * We make sure to trim the String, i.e. delete leftmost and rightmost spaces/newlines.
					 * Then, we get the cards by splitting by newline.
					 */
					String cards[] = request.getParameter("deck").replace("\r", "").trim().split("\n");
					
					if (cards.length != CardRDG.getCardsPerDeck()) {
						failure(request, response, String.format(CARDS_FAIL_MESSAGE, cards.length, CardRDG.getCardsPerDeck()));
					}
					else {
						
						deck = new DeckRDG(DeckRDG.getMaxId(), player);
						deck.insert();
						
						CardRDG cardInDeck = null;
						
						for (String card : cards) {
							
							String type = card.substring(0, 1);
							String name = card.substring(3, card.length() - 1);
							
							cardInDeck = new CardRDG(deck.getId(), type, name);
							cardInDeck.insert();
							
						}
						
						success(request, response, DECK_SUCCESS_MESSAGE);
						
					}
					
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
