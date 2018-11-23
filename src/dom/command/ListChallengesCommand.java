package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.IChallenge;
import dom.model.challenge.mapper.ChallengeInputMapper;

public class ListChallengesCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view the list of challenges.";
	
	@SetInRequestAttribute
	public List<IChallenge> challenges;

	public ListChallengesCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		try {
			checkIfLoggedIn(NOT_LOGGED_IN);
			challenges = ChallengeInputMapper.findAll();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}

}
