package dom.command;

import org.apache.commons.lang3.StringUtils;
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
			
			String path = (String) helper.getRequestAttribute("path");
			String[] parts = StringUtils.split(path, "/");
			
			this.deck = getDeck(Long.parseLong(parts[2]));
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
