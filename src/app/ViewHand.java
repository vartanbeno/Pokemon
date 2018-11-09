package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.CardHelper;
import dom.model.card.rdg.CardRDG;
import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.rdg.CardInPlayRDG;
import dom.model.deck.DeckHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

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
			
			GameRDG gameRDG = getGame(request, response);
			if (gameRDG == null) return;
			
			UserRDG userRDG = UserRDG.findById(getUserId(request));
			UserHelper user = new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "");
			
			DeckRDG deckRDG = DeckRDG.findByPlayer(userRDG.getId());
			DeckHelper deck = new DeckHelper(deckRDG.getId(), user);
			
			List<CardInPlayRDG> handRDG = CardInPlayRDG.findByGameAndPlayerAndStatus(
					gameRDG.getId(), userRDG.getId(), CardStatus.hand.ordinal()
			);
			
			List<CardHelper> hand = new ArrayList<CardHelper>();
			
			for (CardInPlayRDG cardInPlayRDG : handRDG) {
				
				CardRDG cardRDG = CardRDG.findById(cardInPlayRDG.getCard());
				CardHelper card = new CardHelper(cardRDG.getId(), deck, cardRDG.getType(), cardRDG.getName());
				
				hand.add(card);
				
			}
			
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
