package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.rdg.CardRDG;
import dom.model.deck.rdg.DeckRDG;

@WebServlet("/UploadDeck")
public class UploadDeck extends PageController {

	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to upload a deck.";
	private static final String DECK_FAIL_MESSAGE = "You already have a deck.";
	private static final String CARDS_FAIL_MESSAGE = "You have %1$d card(s) in your deck. You must have %2$d.";
	private static final String FORMATTING_ERROR = "Make sure to adhere to the required formatting for each card.";
	private static final String CARD_TYPE_ERROR = "One or more of your cards had an invalid type. Please make sure it's one of e/p/t.";
	
	private static final String DECK_SUCCESS_MESSAGE = "You have successfully uploaded your deck.";
	
    public UploadDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
				
			DeckRDG deck = DeckRDG.findByPlayer(getUserId(request));
			
			if (deck == null) {
				request.setAttribute("numberOfCards", CardRDG.getNumberOfCardsPerDeck());
				request.getRequestDispatcher(Global.UPLOAD_DECK_FORM).forward(request, response);
			}
			else {
				failure(request, response, DECK_FAIL_MESSAGE);
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
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			long player = getUserId(request);
			DeckRDG deck = DeckRDG.findByPlayer(player);
			
			if (deck != null) {
				failure(request, response, DECK_FAIL_MESSAGE);
				return;
			}
			
			/**
			 * We delete carriage returns (\r) by replacing them with an empty String "".
			 * We make sure to trim the String, i.e. delete leftmost and rightmost spaces/newlines.
			 * Then, we get the cards by splitting by newline.
			 */
			String cards[] = request.getParameter("deck").replace("\r", "").trim().split("\n");
			
			if (cards.length != CardRDG.getNumberOfCardsPerDeck()) {
				failure(request, response, String.format(CARDS_FAIL_MESSAGE, cards.length, CardRDG.getNumberOfCardsPerDeck()));
			}
			else {
				
				boolean deckIsValid = true;
				
				CardSpec cardSpec = null;
				List<CardSpec> cardSpecs = new ArrayList<CardSpec>();
				String type, name = "";
				
				for (String card : cards) {
					
					try {
						type = card.substring(0, 1);
						name = card.substring(3, card.length() - 1);
					}
					catch (StringIndexOutOfBoundsException e) {
						failure(request, response, FORMATTING_ERROR);
						return;
					}
					
					if (!type.equals("e") && !type.equals("p") && !type.equals("t")) {
						deckIsValid = false;
						break;
					}
					
					cardSpec = new CardSpec(type, name);
					cardSpecs.add(cardSpec);
					
				}
				
				if (deckIsValid) {
					
					deck = new DeckRDG(DeckRDG.getMaxId(), player);
					deck.insert();
					
					CardRDG cardInDeck = null;
					for (CardSpec card : cardSpecs) {
						cardInDeck = new CardRDG(CardRDG.getMaxId(), deck.getId(), card.type, card.name);
						cardInDeck.insert();
					}
					
					success(request, response, DECK_SUCCESS_MESSAGE);
					
				}
				else {
					failure(request, response, CARD_TYPE_ERROR);
				}
				
			}
				
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}
	
	private class CardSpec {
		
		public String type;
		public String name;
		
		public CardSpec(String type, String name) {
			this.type = type;
			this.name = name;
		}
		
	}
	
}
