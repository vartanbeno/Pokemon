package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserMapper;

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
			
			User player = UserMapper.findById(getUserId(request));
			Deck deck = DeckMapper.findByPlayer(player.getId());
			
			if (deck == null) {
				failure(request, response, NO_DECK);
				return;
			}
						
			request.setAttribute("deck", deck);
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
