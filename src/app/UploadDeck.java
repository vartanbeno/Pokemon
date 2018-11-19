package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.Card;
import dom.model.card.ICard;
import dom.model.card.tdg.CardTDG;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.deck.tdg.DeckTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

@WebServlet("/UploadDeck")
public class UploadDeck extends PageController {

	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to upload a deck.";
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
				
			int numberOfDecks = DeckMapper.findByPlayer(getUserId(request)).size();
			int numberOfCards = CardTDG.getNumberOfCardsPerDeck();
			
			request.setAttribute("numberOfDecks", numberOfDecks);
			request.setAttribute("numberOfCards", numberOfCards);
			request.getRequestDispatcher(Global.UPLOAD_DECK_FORM).forward(request, response);
			
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
			
			User player = UserInputMapper.findById(getUserId(request));
			
			/**
			 * We delete carriage returns (\r) by replacing them with an empty String "".
			 * We make sure to trim the String, i.e. delete leftmost and rightmost spaces/newlines.
			 * Then, we get the cards by splitting by newline.
			 */
			String cardsArray[] = request.getParameter("deck").replace("\r", "").trim().split("\n");
			
			if (cardsArray.length != CardTDG.getNumberOfCardsPerDeck()) {
				failure(request, response, String.format(CARDS_FAIL_MESSAGE, cardsArray.length, CardTDG.getNumberOfCardsPerDeck()));
			}
			else {
				
				List<ICard> cards = new ArrayList<ICard>();
				Deck deck = new Deck(DeckTDG.getMaxId(), 1, player, cards);
				
				for (String cardString : cardsArray) {
					
					String type, name = "";
					
					try {
						type = cardString.substring(0, 1);
						name = cardString.substring(3, cardString.length() - 1);
					}
					catch (StringIndexOutOfBoundsException e) {
						failure(request, response, FORMATTING_ERROR);
						return;
					}
					
					if (!type.equals("e") && !type.equals("p") && !type.equals("t")) {
						failure(request, response, CARD_TYPE_ERROR);
						return;
					}
					
					Card card = new Card(CardTDG.getMaxId(), 1, deck.getId(), type, name);
					cards.add(card);
					
				}
				
				deck.setCards(cards);
				DeckMapper.insertStatic(deck);
				
				success(request, response, DECK_SUCCESS_MESSAGE);
				
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
