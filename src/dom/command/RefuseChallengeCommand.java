package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.ChallengeFactory;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.IChallenge;

public class RefuseChallengeCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to refuse a challenge.";
	
	private static final String REFUSE_SUCCESS = "You have successfully refused %s's challenge.";

	public RefuseChallengeCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long challengeId = Long.parseLong((String) helper.getRequestAttribute("challenge"));
			long challengeVersion = getVersion();
			
			IChallenge challenge = getChallengeToAcceptOrRefuse(challengeId);
			challenge.setStatus(ChallengeStatus.refused.ordinal());
			
			ChallengeFactory.registerDirty(
					challenge.getId(),
					challengeVersion,
					challenge.getChallenger(),
					challenge.getChallengee(),
					challenge.getStatus(),
					challenge.getChallengerDeck()
			);
			
			this.message = String.format(REFUSE_SUCCESS, challenge.getChallenger().getUsername());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
