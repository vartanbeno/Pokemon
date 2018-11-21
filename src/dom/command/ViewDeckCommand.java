package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckInputMapper;

public class ViewDeckCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view a deck.";
	
	@SetInRequestAttribute
	public IDeck deck;
	
	@SetInRequestAttribute
	public List<IDeck> decks;
	
	public ViewDeckCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			if (helper.getString("deck") != null) {
				this.deck = getDeck();
			}
			else {
				this.decks = DeckInputMapper.findByPlayer(getUserId());
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
