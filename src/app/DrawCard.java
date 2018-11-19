package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

import dom.model.card.Card;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.CardInPlayFactory;
import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.tdg.CardInPlayTDG;
import dom.model.deck.Deck;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameStatus;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.User;

@WebServlet("/DrawCard")
public class DrawCard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NO_MORE_CARDS = "You have no more cards left in your deck to draw.";
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
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
			
			Game game = getGame(request, response);
			if (game == null) return;
			
			if (game.getStatus() != GameStatus.ongoing.ordinal()) {
				failure(request, response, GAME_STOPPED);
				return;
			}
			
			long userId = getUserId(request);
			
			GameBoard gameBoard = GameInputMapper.buildGameBoard(game);
			User player = null;
			Deck deck = null;
			
			if (userId == gameBoard.getGame().getChallenger().getId()) {
				player = (User) gameBoard.getGame().getChallenger();
				deck = (Deck) gameBoard.getGame().getChallengerDeck(); 
			}
			else if (userId == gameBoard.getGame().getChallengee().getId()) {
				player = (User) gameBoard.getGame().getChallengee(); 
				deck = (Deck) gameBoard.getGame().getChallengeeDeck();
			}
			
			if (deck.getCards().size() == 0) {
				failure(request, response, NO_MORE_CARDS);
				return;
			}
			
			Card card = (Card) deck.getCards().remove(0);
			
			CardInPlay cardInPlay = new CardInPlay(
					CardInPlayTDG.getMaxId(), 1,
					game, player, deck, card,
					CardStatus.hand.ordinal()
			);
			
			CardInPlayFactory.createNew(cardInPlay);
			UoW.getCurrent().commit();
			
			success(request, response, String.format(DRAW_SUCCESS, card.getType(), card.getName()));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

}
