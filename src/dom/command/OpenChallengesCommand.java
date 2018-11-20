package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.IChallenge;
import dom.model.challenge.mapper.ChallengeInputMapper;
import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckInputMapper;

public class OpenChallengesCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your open challenges.";
	
	@SetInRequestAttribute(attributeName = "decks")
	public List<IDeck> myDecks;
	
	@SetInRequestAttribute
	public List<IChallenge> challengesAgainstMe;
	
	@SetInRequestAttribute
	public List<IChallenge> challengesAgainstOthers;
	
	public OpenChallengesCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long id = getUserId();
			
			myDecks = DeckInputMapper.findByPlayer(id);
			challengesAgainstMe = ChallengeInputMapper.findOpenByChallengee(id);
			challengesAgainstOthers = ChallengeInputMapper.findOpenByChallenger(id);
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
