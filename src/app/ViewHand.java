package app;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayMapper;
import dom.model.game.Game;
import dom.model.user.User;

@WebServlet("/ViewHand")
public class ViewHand extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your hand.";
       
    public ViewHand() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			Game game = getGame(request, response);
			if (game == null) return;
			
			long userId = getUserId(request);
			User player = userId == game.getChallenger().getId() ? (User) game.getChallenger() : (User) game.getChallengee();
			
			List<ICardInPlay> hand = CardInPlayMapper.findByGameAndPlayerAndStatus(
					game.getId(), player.getId(), CardStatus.hand.ordinal()
			);
			
			request.setAttribute("hand", hand);
			request.getRequestDispatcher(Global.VIEW_HAND).forward(request, response);
			
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
