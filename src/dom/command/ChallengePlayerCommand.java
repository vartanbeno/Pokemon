package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.mapper.ChallengeInputMapper;
import dom.model.deck.IDeck;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class ChallengePlayerCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to challenge a player.";
	
	private static final String CHALLENGEE_ID_FORMAT = "You must provide a valid format for the challengee ID (positive integer).";
	private static final String ALREADY_CHALLENGED = "You already have an open challenge against %s.";
	private static final String CHALLENGEE_DOES_NOT_EXIST = "You have issued a challenge to a user that doesn't exist.";
	private static final String SAME_ID = "You cannot challenge yourself.";
	
	private static final String NO_DECK = "You must have at least one deck to issue a challenge to a player.";
	private static final String NOT_YOUR_DECK = "The deck you specified is not yours.";
	
	private static final String CHALLENGE_SUCCESS = "You have successfully challenged %s to a game.";
	
	public ChallengePlayerCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long challengerId = getUserId();
			
			/**
			 * Do I have at least one deck?
			 */
			List<IDeck> myDecks = getMyDecks();
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
			 * First of all, does the deck with the specified deck ID exist?
			 * Second of all, if it does, is it mine?
			 */
			IDeck myDeck = getDeck();
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
			
			ChallengeFactory.createNew(challenger, challengee, myDeck);
			
			this.message = String.format(CHALLENGE_SUCCESS, challengee.getUsername());
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
