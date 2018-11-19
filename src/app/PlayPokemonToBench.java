package app;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayInputMapper;
import dom.model.card.Card;
import dom.model.card.mapper.CardInputMapper;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.CardInPlayFactory;
import dom.model.game.Game;
import dom.model.game.GameStatus;

@WebServlet("/PlayPokemonToBench")
public class PlayPokemonToBench extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String CARD_INDEX_FORMAT = "You must provide a valid format for the card index (positive integer).";
	private static final String BENCH_IS_FULL = "Your bench is full. It already has 5 Pokemon.";
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot bench it.";
	private static final String NOT_A_POKEMON = "You can only bench cards of type 'p', i.e. Pokemon.";
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
	private static final String EMPTY_HAND = "You do not have any cards in your hand.";
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String BENCH_SUCCESS = "You have sent %s to the bench!";
	
    public PlayPokemonToBench() {
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
			
			Game game = getGame(request, response);
			if (game == null) return;
			
			if (game.getStatus() != GameStatus.ongoing.ordinal()) {
				failure(request, response, GAME_STOPPED);
				return;
			}
			
			long userId = getUserId(request);
			
			/**
			 * This is the card index.
			 * Example: let's say your hand is: [2, 5, 9, 12, 27]
			 * Going off of a zero-based index:
			 * If you choose card 0, you get 2.
			 * If you choose card 3, you get 12.
			 * If you choose card 10, you should get an error.
			 * etc.
			 */
			final int cardIndex;
			
			try {
				cardIndex = Integer.parseInt(request.getParameter("card"));
			}
			catch (NumberFormatException e) {
				failure(request, response, CARD_INDEX_FORMAT);
				return;
			}
			
			List<ICardInPlay> playerBench = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
					game.getId(), userId, CardStatus.benched.ordinal()
			);
			
			if (playerBench.size() >= 5) {
				failure(request, response, BENCH_IS_FULL);
				return;
			}
			
			List<ICardInPlay> playerHand = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
					game.getId(), userId, CardStatus.hand.ordinal()
			);
			
			if (playerHand.size() == 0) {
				failure(request, response, EMPTY_HAND);
				return;
			}
			
			CardInPlay playerCardInHand;
			try {
				playerCardInHand = (CardInPlay) playerHand.get(cardIndex);
			}
			catch (IndexOutOfBoundsException e) {
				failure(request, response, NOT_IN_HAND);
				return;
			}
			
			CardInPlay cardInHand = (CardInPlay) CardInPlayInputMapper.findByCard(playerCardInHand.getCard().getId());
			Card card = CardInputMapper.findById(cardInHand.getCard().getId());
			
			if (card.getType().equals("p")) {
				
				cardInHand.setStatus(CardStatus.benched.ordinal());
				
				CardInPlayFactory.registerDirty(cardInHand);
				UoW.getCurrent().commit();
				
				success(request, response, String.format(BENCH_SUCCESS, card.getName()));
				
			}
			else {
				failure(request, response, NOT_A_POKEMON);
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
