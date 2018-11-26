package dom.command;

import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.ICard;
import dom.model.deck.IDeck;
import dom.model.discard.DiscardFactory;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameFactory;
import dom.model.game.mapper.GameInputMapper;
import dom.model.hand.HandFactory;
import dom.model.hand.IHand;
import dom.model.user.IUser;

public class EndTurnCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String END_TURN_SUCCESS = "You have ended your turn. %s just drew a card.";
	private static final String END_TURN_SUCCESS_AND_DISCARD = "You have ended your turn. %s just drew a card and you discarded one from your hand.";
	
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
			game.setTurn(game.getTurn() + 1);
			GameFactory.registerDirty(game);
			
			/**
			 * We need some game board data, since it contains current state of decks
			 * i.e. number of cards left in each deck.
			 * Also the player's hand (player who ended the turn). We'll need that to check its size.
			 */
			GameBoard gameInfo = GameInputMapper.buildGameBoard(game);
			
			List<IHand> previousTurnHand = new ArrayList<IHand>();
			IDeck nextTurnDeck = null;
			
			if (nextTurnPlayer.getId() == game.getChallenger().getId()) {
				previousTurnHand = gameInfo.getChallengeeHand();
				nextTurnDeck = gameInfo.getGame().getChallengerDeck();
			}
			else if (nextTurnPlayer.getId() == game.getChallengee().getId()) {
				previousTurnHand = gameInfo.getChallengerHand();
				nextTurnDeck = gameInfo.getGame().getChallengeeDeck();
			}
			
			/**
			 * When a player ends their turn, the other player draws a card.
			 */
			ICard card = nextTurnDeck.getCards().remove(0);
			HandFactory.createNew(
					game, nextTurnPlayer, nextTurnDeck.getId(), card
			);
			
			this.message = String.format(END_TURN_SUCCESS, nextTurnPlayer.getUsername());
			
			/**
			 * If the player's (the one who ended the turn) hand size is greater than 7,
			 * move the oldest card to their discard pile.
			 */
			if (previousTurnHand.size() > 7) {
				
				IHand cardToDiscard = previousTurnHand.remove(0);
				
				HandFactory.registerDeleted(cardToDiscard);
				DiscardFactory.createNew(
						cardToDiscard.getGame(),
						cardToDiscard.getPlayer(),
						cardToDiscard.getDeck(),
						cardToDiscard.getCard()
				);
				
				this.message = String.format(END_TURN_SUCCESS_AND_DISCARD, nextTurnPlayer.getUsername());
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
