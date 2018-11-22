package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.ChallengeStatus;

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
						
			Challenge challenge = getChallengetoWithdrawFrom();
			challenge.setStatus(ChallengeStatus.withdrawn.ordinal());
			
			ChallengeFactory.registerDirty(challenge);
			
			this.message = String.format(WITHDRAW_SUCCESS, challenge.getChallengee().getUsername());
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
