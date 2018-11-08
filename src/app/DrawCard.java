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
import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.rdg.CardInPlayRDG;
import dom.model.deck.DeckHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/DrawCard")
public class DrawCard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String INVALID_GAME = "This is not a valid game.";
	private static final String WRONG_FORMAT = "You must specify a game ID in the correct format (a positive integer).";
	private static final String NOT_YOUR_GAME = "You must be part of the game to draw a card.";
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
			
			if (loggedIn(request)) {
				
				long playerId = getUserId(request);
				Long gameId = null;
				
				try {
					gameId = Long.parseLong(request.getParameter("game"));
				}
				catch (NumberFormatException e) {
					failure(request, response, WRONG_FORMAT);
					return;
				}
				
				GameRDG gameRDG = GameRDG.findById(gameId);
				
				if (gameRDG == null) {
					failure(request, response, INVALID_GAME);
					return;
				}
				else if (playerId != gameRDG.getChallenger() && playerId != gameRDG.getChallengee()) {
					failure(request, response, NOT_YOUR_GAME);
					return;
				}
				
				UserRDG playerRDG = UserRDG.findById(playerId);
				
				UserHelper player = new UserHelper(
						playerRDG.getId(),
						playerRDG.getVersion(),
						playerRDG.getUsername(),
						""
				);
				
				List<CardInPlayRDG> playerHandRDGs = CardInPlayRDG.findByGameAndPlayerAndStatus(
						gameRDG.getId(), playerRDG.getId(), CardStatus.hand.ordinal()
				);
				
				DeckRDG playerDeckRDG = DeckRDG.findByPlayer(playerRDG.getId());
				DeckHelper playerDeck = new DeckHelper(playerDeckRDG.getId(), player);
				
				List<CardRDG> playerCardRDGs = CardRDG.findByDeck(playerDeckRDG.getId());
				List<CardHelper> playerCards = new ArrayList<CardHelper>();
				CardHelper playerCard = null;
				
				for (CardRDG playerCardRDG : playerCardRDGs) {
					
					playerCard = new CardHelper(
							playerCardRDG.getId(),
							playerDeck,
							playerCardRDG.getType(),
							playerCardRDG.getName()
					);
					
					playerCards.add(playerCard);
					
				}
				
				IntStream.range(0, playerHandRDGs.size()).forEach($ -> playerCards.remove(0));
				
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
			else {
				failure(request, response, NOT_LOGGED_IN);
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
