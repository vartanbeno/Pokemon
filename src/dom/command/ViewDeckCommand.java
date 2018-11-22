package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.deck.IDeck;

public class ViewDeckCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view a deck.";
	
	@SetInRequestAttribute
	public IDeck deck;
	
	public ViewDeckCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		try {
			checkIfLoggedIn(NOT_LOGGED_IN);
			long deckId = Long.parseLong((String) helper.getRequestAttribute("deck"));
			this.deck = getDeck(deckId);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}

}
