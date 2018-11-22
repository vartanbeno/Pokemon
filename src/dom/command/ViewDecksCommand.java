package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.deck.IDeck;

public class ViewDecksCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your decks.";
	
	@SetInRequestAttribute(attributeName = "decks")
	public List<IDeck> myDecks;
	
	public ViewDecksCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		try {
			checkIfLoggedIn(NOT_LOGGED_IN);
			myDecks = getMyDecks();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}

}
