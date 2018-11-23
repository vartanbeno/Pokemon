package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.ICard;
import dom.model.cardinplay.CardInPlayFactory;
import dom.model.deck.IDeck;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameFactory;
import dom.model.game.GameStatus;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.IUser;

public class EndTurnCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String NOT_YOUR_GAME = "You are not part of this game.";
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
	private static final String NOT_YOUR_TURN = "It's not your turn yet.";
	
	private static final String END_TURN_SUCCESS = "You have ended your turn. %s just drew a card.";
	
	public EndTurnCommand(Helper helper) {
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
			if (userId != game.getCurrentTurn()) throw new CommandException(NOT_YOUR_TURN);
			
			IUser nextTurnPlayer = game.getCurrentTurn() == game.getChallenger().getId() ?
					game.getChallengee() : game.getChallenger();
					
			game.setCurrentTurn(nextTurnPlayer.getId());
			GameFactory.registerDirty(game);
			
			/**
			 * We need some game board data, since it contains current state of decks
			 * i.e. number of cards left in each deck.
			 */
			GameBoard gameInfo = GameInputMapper.buildGameBoard(game);
			IDeck nextTurnDeck = game.getCurrentTurn() == game.getChallenger().getId() ?
					gameInfo.getGame().getChallengerDeck() : gameInfo.getGame().getChallengeeDeck();
			
			/**
			 * When a player ends their turn, the other player draws a card.
			 */
			ICard drawnCard = nextTurnDeck.getCards().remove(0);
			CardInPlayFactory.createNew(
					game, nextTurnPlayer, nextTurnDeck, drawnCard
			);
			
			this.message = String.format(END_TURN_SUCCESS, nextTurnPlayer.getUsername());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
