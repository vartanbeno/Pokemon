package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.Card;
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
	
	private static final String NOT_YOUR_GAME = "You are not part of this game.";
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
	private static final String NO_MORE_CARDS = "You have no more cards left in your deck to draw.";
	
	private static final String DRAW_SUCCESS = "You have successfully drawn a card! %s: %s.";
	
	public DrawCardCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			long userId = getUserId();
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			Game game = getGame(gameId);
			
			if (userId != game.getChallenger().getId() && userId != game.getChallengee().getId())
				throw new CommandException(NOT_YOUR_GAME);
			if (game.getStatus() != GameStatus.ongoing.ordinal()) throw new CommandException(GAME_STOPPED);
						
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
			
			/**
			 * Register the game as well, since we want to increment its version.
			 * Drawing a card counts as a turn, meaning the game is in a different state.
			 */
			CardInPlayFactory.createNew(game, player, deck, card);
			GameFactory.registerDirty(game);
			
			this.message = String.format(DRAW_SUCCESS, card.getType(), card.getName());
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
