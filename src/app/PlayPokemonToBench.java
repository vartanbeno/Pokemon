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

@WebServlet("/PlayPokemonToBench")
public class PlayPokemonToBench extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CARD_ID_FORMAT = "You must provide a valid format for the card ID (positive integer).";
	private static final String BENCH_IS_FULL = "Your bench is full. It already has 5 Pokemon.";
	private static final String ALREADY_BENCHED = "That card is already on the bench.";
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot bench it.";
	private static final String NOT_A_POKEMON = "You can only bench cards of type 'p', i.e. Pokemon.";
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String BENCH_SUCCESS = "You have sent %s to the bench!";
	
    public PlayPokemonToBench() {
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
			
			List<CardInPlayRDG> playerBenchRDG = CardInPlayRDG.findByGameAndPlayerAndStatus(
					gameRDG.getId(), getUserId(request), CardStatus.benched.ordinal()
			);
			
			if (playerBenchRDG.size() >= 5) {
				failure(request, response, BENCH_IS_FULL);
				return;
			}
			
			List<CardInPlayRDG> playerHandRDG = CardInPlayRDG.findByGameAndPlayerAndStatus(
					gameRDG.getId(), getUserId(request), CardStatus.hand.ordinal()
			);
			
			UserRDG userRDG = UserRDG.findById(getUserId(request));
			UserHelper user = new UserHelper(
					userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), ""
			);
			
			DeckRDG deckRDG = DeckRDG.findByPlayer(userRDG.getId());
			DeckHelper deck = new DeckHelper(deckRDG.getId(), user);
			
			List<CardHelper> cards = new ArrayList<CardHelper>();
			
			for (CardInPlayRDG cardInHand : playerHandRDG) {
				
				CardRDG cardRDG = CardRDG.findById(cardInHand.getCard());
				
				/**
				 * You can only play Pokemon to the bench.
				 */
				if (cardRDG.getType().equals("p")) {
					CardHelper card = new CardHelper(cardRDG.getId(), deck, cardRDG.getType(), cardRDG.getName());
					cards.add(card);
				}
				
			}
			
			request.setAttribute("benchSize", playerBenchRDG.size());
			request.setAttribute("cards", cards);
			request.getRequestDispatcher(Global.PLAY_TO_BENCH_FORM).forward(request, response);
			
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
			
			GameRDG gameRDG = getGame(request, response);
			if (gameRDG == null) return;
			
			final Long cardId;
			
			try {
				cardId = Long.parseLong(request.getParameter("card"));
			}
			catch (NumberFormatException e) {
				failure(request, response, CARD_ID_FORMAT);
				return;
			}
			
			List<CardInPlayRDG> playerBenchRDG = CardInPlayRDG.findByGameAndPlayerAndStatus(
					gameRDG.getId(), getUserId(request), CardStatus.benched.ordinal()
			);
			
			if (playerBenchRDG.size() >= 5) {
				failure(request, response, BENCH_IS_FULL);
				return;
			}
			
			boolean alreadyBenched =
					playerBenchRDG.stream().filter(cardInHand -> cardInHand.getCard() == cardId).findFirst().isPresent();
			
			if (alreadyBenched) {
				failure(request, response, ALREADY_BENCHED);
				return;
			}
			
			List<CardInPlayRDG> playerHandRDG = CardInPlayRDG.findByGameAndPlayerAndStatus(
					gameRDG.getId(), getUserId(request), CardStatus.hand.ordinal()
			);
			
			boolean inHand =
					playerHandRDG.stream().filter(cardInHand -> cardInHand.getCard() == cardId).findFirst().isPresent();
			
			if (inHand) {
				
				CardInPlayRDG cardInHand = CardInPlayRDG.findByCard(cardId);
				CardRDG card = CardRDG.findById(cardInHand.getCard());
				
				if (card.getType().equals("p")) {
					cardInHand.setStatus(CardStatus.benched.ordinal());
					cardInHand.update();
					success(request, response, String.format(BENCH_SUCCESS, card.getName()));
				}
				else {
					failure(request, response, NOT_A_POKEMON);
				}
				
			}
			else {
				failure(request, response, NOT_IN_HAND);
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
