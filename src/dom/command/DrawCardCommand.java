package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.uow.UoW;

import dom.model.card.Card;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.CardInPlayFactory;
import dom.model.deck.Deck;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameFactory;
import dom.model.game.GameStatus;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.User;

public class DrawCardCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
	private static final String NO_MORE_CARDS = "You have no more cards left in your deck to draw.";
	
	private static final String DRAW_SUCCESS = "You have successfully drawn a card! %s: %s.";
	
	@SetInRequestAttribute
	public String message;
	
	public DrawCardCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			Game game = getGame();
			if (game.getStatus() != GameStatus.ongoing.ordinal()) throw new CommandException(GAME_STOPPED);
			
			long userId = getUserId();
			
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
			
			if (deck.getCards().size() == 0) throw new CommandException(NO_MORE_CARDS);
			
			Card card = (Card) deck.getCards().remove(0);
			
			CardInPlay cardInPlay = CardInPlayFactory.createNew(game, player, deck, card);
			
			/**
			 * Register the game as well, since we want to increment its version.
			 * Drawing a card counts as a turn, meaning the game is in a different state.
			 */
			GameFactory.registerDirty(game);
			CardInPlayFactory.createNew(cardInPlay);
			
			UoW.getCurrent().commit();
			
			message = String.format(DRAW_SUCCESS, card.getType(), card.getName());
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
