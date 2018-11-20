package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.uow.UoW;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.mapper.ChallengeInputMapper;
import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class ChallengePlayerCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to challenge a player.";
	
	private static final String CHALLENGEE_ID_FORMAT = "You must provide a valid format for the challengee ID (positive integer).";
	private static final String ALREADY_CHALLENGED = "You already have an open challenge against %s.";
	private static final String CHALLENGEE_DOES_NOT_EXIST = "You have issued a challenge to a user that doesn't exist.";
	private static final String SAME_ID = "You cannot challenge yourself.";
	
	private static final String NO_DECK = "You must have at least one deck to issue a challenge to a player.";
	private static final String DECK_ID_FORMAT = "You must provide a valid format for your deck ID (positive integer).";
	private static final String DECK_DOES_NOT_EXIST = "The deck you specified does not exist.";
	private static final String NOT_YOUR_DECK = "The deck you specified is not yours.";
	
	@SetInRequestAttribute(attributeName = "username")
	public String challengee;
	
	public ChallengePlayerCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			/**
			 * Do I have at least one deck?
			 */
			long challengerId = getUserId();
			List<IDeck> myDecks = DeckInputMapper.findByPlayer(challengerId);
			if (myDecks.isEmpty()) throw new CommandException(NO_DECK);
			
			/**
			 * Did I provide a valid format for the challengee ID?
			 */
			Long challengeeId = null;
			try {
				challengeeId = helper.getLong("player");
			}
			catch (NumberFormatException e) {
				throw new CommandException(CHALLENGEE_ID_FORMAT);
			}
			
			/**
			 * Did I somehow challenge myself (challenger == challengee)?
			 */
			if (challengerId == challengeeId) throw new CommandException(SAME_ID);
			
			/**
			 * Did I provide a valid format for the ID of the deck I want to use?
			 */
			Long myDeckId = null;
			try {
				myDeckId = helper.getLong("deck");
			}
			catch (NumberFormatException e) {
				throw new CommandException(DECK_ID_FORMAT);
			}
			
			/**
			 * First of all, does the deck with the specified deck ID exist?
			 * Second of all, if it does, is it mine?
			 */
			IDeck myDeck = DeckInputMapper.findById(myDeckId);
			if (myDeck == null) throw new CommandException(DECK_DOES_NOT_EXIST);
			if (myDeck.getPlayer().getId() != challengerId) throw new CommandException(NOT_YOUR_DECK);
			
			/**
			 * You cannot challenge a player if you already have an open challenge against them.
			 */
			Challenge challenge = ChallengeInputMapper.findOpenByChallengerAndChallengee(challengerId, challengeeId);
			if (challenge != null) throw new CommandException(String.format(ALREADY_CHALLENGED, challenge.getChallengee().getUsername()));
			
			User challenger = UserInputMapper.findById(challengerId);
			User challengee = UserInputMapper.findById(challengeeId);
			
			/**
			 * The challengee ID I provided isn't attached to a user in the database.
			 */
			if (challengee == null) throw new CommandException(CHALLENGEE_DOES_NOT_EXIST);
			
			this.challengee = challengee.getUsername();
			
			ChallengeFactory.createNew(challenger, challengee, myDeck);
			UoW.getCurrent().commit();
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
