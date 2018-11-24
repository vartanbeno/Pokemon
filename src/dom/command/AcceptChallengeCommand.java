package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.Card;
import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.ChallengeStatus;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.game.Game;
import dom.model.game.GameFactory;
import dom.model.game.GameStatus;
import dom.model.hand.HandFactory;

public class AcceptChallengeCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to accept a challenge.";
	
	private static final String NO_DECK = "You must have a deck to accept a challenge.";
	
	private static final String ACCEPT_SUCCESS = "You have successfully accepted %1$s's challenge. Game %2$d has begun! %1$s drew their first card!";

	public AcceptChallengeCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			List<IDeck> myDecks = getMyDecks();
			if (myDecks.size() == 0) throw new CommandException(NO_DECK);
			
			long challengeId = Long.parseLong((String) helper.getRequestAttribute("challenge"));
			Challenge challenge = getChallengeToAcceptOrRefuse(challengeId);
			
			Deck myDeck = getDeck();
			checkIfItsMyDeck(myDeck);
			
			challenge.setStatus(ChallengeStatus.accepted.ordinal());
			
			ChallengeFactory.registerDirty(challenge);
			
			Game game = GameFactory.createNew(
					challenge.getChallenger(),
					challenge.getChallengee(),
					challenge.getChallengerDeck(),
					myDeck,
					challenge.getChallenger().getId(),
					GameStatus.ongoing.ordinal()
			);
			
			/**
			 * When a game first starts, the challenger draws a card.
			 * The card is at the top of the deck, so the first in the list is drawn.
			 */
			Card card = (Card) game.getChallengerDeck().getCards().remove(0);
			HandFactory.createNew(
					game, game.getChallenger(), game.getChallengerDeck().getId(), card
			);
			
			this.message = String.format(ACCEPT_SUCCESS, challenge.getChallenger().getUsername(), game.getId());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
