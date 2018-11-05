package servlet;

import java.io.IOException;
import java.util.ArrayList;
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
	
	private static final String LOGIN_ERROR_MESSAGE = "You must be logged in to upload a deck.";
	private static final String CARDS_ERROR_MESSAGE = "You have %1$d cards in your deck. You must have %2$d.";
	
	private static final String DECK_SUCCESS_MESSAGE = "Deck successfully uploaded.";
	
    public UploadDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
//				// TODO
				
				
			}
			else {
				failure(request, response, LOGIN_ERROR_MESSAGE);
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
				
				// TODO
				
			}
			else {
				failure(request, response, LOGIN_ERROR_MESSAGE);
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
