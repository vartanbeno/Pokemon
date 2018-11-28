package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.IChallenge;

public class WithdrawFromChallengeCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to withdraw from a challenge.";
	
	private static final String WITHDRAW_SUCCESS = "You have successfully withdrawn from your challenge against %s.";

	public WithdrawFromChallengeCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
						
			long challengeId = Long.parseLong((String) helper.getRequestAttribute("challenge"));
			IChallenge challenge = getChallengeToWithdrawFrom(challengeId);
			
			challenge.setStatus(ChallengeStatus.withdrawn.ordinal());
			
			ChallengeFactory.registerDirty(challenge);
			
			this.message = String.format(WITHDRAW_SUCCESS, challenge.getChallengee().getUsername());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
