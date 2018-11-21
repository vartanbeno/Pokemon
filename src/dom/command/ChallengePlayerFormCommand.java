package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.user.IUser;
import dom.model.user.mapper.UserInputMapper;

public class ChallengePlayerFormCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to challenge a player.";
	
	@SetInRequestAttribute(attributeName = "users")
	public List<IUser> challengees;
	
	@SetInRequestAttribute(attributeName = "decks")
	public List<IDeck> myDecks;
	
	public ChallengePlayerFormCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long id = getUserId();
			
			challengees = UserInputMapper.findAll();
			/**
			 * Shouldn't be able to challenge yourself.
			 * So we filter you out from the list of challengees.
			 */
			challengees.removeIf(challengee -> challengee.getId() == id);
			
			myDecks = DeckInputMapper.findByPlayer(id);
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
