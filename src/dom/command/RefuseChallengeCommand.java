package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.ChallengeStatus;

public class RefuseChallengeCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to refuse/withdraw from a challenge.";
	
	private static final String REFUSE_SUCCESS = "You have successfully refused %s's challenge.";
	private static final String WITHDRAW_SUCCESS = "You have successfully withdrawn from your challenge against %s.";

	public RefuseChallengeCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			Challenge challenge = getChallengeToRefuseOrWithdraw();
			
			/**
			 * If you are the one being challenged and refuse, set the challenge status to refused.
			 * Else if you are the challenger and refuse, set the challenge status to withdrawn.
			 */
			if (challenge.getChallengee().getId() == getUserId()) {
				challenge.setStatus(ChallengeStatus.refused.ordinal());
				this.message = String.format(REFUSE_SUCCESS, challenge.getChallenger().getUsername());
			}
			else if (challenge.getChallenger().getId() == getUserId()) {
				challenge.setStatus(ChallengeStatus.withdrawn.ordinal());
				this.message = String.format(WITHDRAW_SUCCESS, challenge.getChallengee().getUsername());
			}
			
			ChallengeFactory.registerDirty(challenge);
			
		}
		catch (Exception e) {
			this.message = e.getMessage();
			throw new CommandException(e.getMessage());
		}
		
	}

}
