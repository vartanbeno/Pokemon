package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.CardHelper;
import dom.model.card.rdg.CardRDG;
import dom.model.cardinplay.rdg.CardInPlayRDG;
import dom.model.deck.DeckHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/DrawCard")
public class DrawCard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NO_MORE_CARDS = "You have no more cards left in your deck to draw.";
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String DRAW_SUCCESS = "You have successfully drawn a card! %s: %s.";
       
    public DrawCard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			GameRDG gameRDG = getGame(request, response);
			if (gameRDG == null) return;
			
			long playerId = getUserId(request);
			
			UserRDG playerRDG = UserRDG.findById(playerId);
			UserHelper player = new UserHelper(
					playerRDG.getId(),
					playerRDG.getVersion(),
					playerRDG.getUsername(),
					""
			);
			
			List<CardInPlayRDG> playerCardsInPlayRDGs = CardInPlayRDG.findByGameAndPlayer(
					gameRDG.getId(), playerRDG.getId()
			);
			
			DeckRDG playerDeckRDG = DeckRDG.findByPlayer(playerRDG.getId());
			DeckHelper playerDeck = new DeckHelper(playerDeckRDG.getId(), player);
			
			List<CardRDG> playerCardRDGs = CardRDG.findByDeck(playerDeckRDG.getId());
			List<CardHelper> playerCards = new ArrayList<CardHelper>();
			
			for (CardRDG playerCardRDG : playerCardRDGs) {
				
				CardHelper playerCard = new CardHelper(
						playerCardRDG.getId(),
						playerDeck,
						playerCardRDG.getType(),
						playerCardRDG.getName()
				);
				
				playerCards.add(playerCard);
				
			}
			
			IntStream.range(0, playerCardsInPlayRDGs.size()).forEach($ -> playerCards.remove(0));
			
			if (playerCards.size() == 0) {
				failure(request, response, NO_MORE_CARDS);
				return;
			}
			
			CardHelper drawnCard = playerCards.remove(0);
			
			CardInPlayRDG drawnCardRDG = new CardInPlayRDG(
					CardInPlayRDG.getMaxId(),
					gameRDG.getId(),
					playerId,
					playerDeckRDG.getId(),
					drawnCard.getId()
			);
			drawnCardRDG.insert();
			
			success(request, response, String.format(DRAW_SUCCESS, drawnCard.getType(), drawnCard.getName()));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

}
