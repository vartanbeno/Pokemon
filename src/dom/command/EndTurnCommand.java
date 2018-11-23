package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.ICard;
import dom.model.cardinplay.CardInPlayFactory;
import dom.model.deck.IDeck;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameFactory;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.IUser;

public class EndTurnCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String END_TURN_SUCCESS = "You have ended your turn. %s just drew a card.";
	
	public EndTurnCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			Game game = getGame(gameId);
			
			checkIfImPartOfGame(game);
			checkIfGameHasEnded(game);
			checkIfItsMyTurn(game);
			
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
