package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Deck")
public class Deck extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view a deck.";
	
    public Deck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			if (request.getParameter("deck") == null) {
				request.setAttribute("decks", getMyDecks(request));
				request.getRequestDispatcher(Global.VIEW_DECKS).forward(request, response);
			}
			else {
				dom.model.deck.Deck deck = getDeck(request, response);
				if (deck == null) return;
				request.setAttribute("deck", deck);
				request.getRequestDispatcher(Global.VIEW_DECK).forward(request, response);
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
		UploadDeck uploadDeck = new UploadDeck();
		uploadDeck.doPost(request, response);
	}

}
