package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.tdg.CardTDG;
import dom.model.deck.mapper.DeckInputMapper;

public class UploadDeckFormCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to upload a deck.";
	
	@SetInRequestAttribute
	public int numberOfDecks;
	
	@SetInRequestAttribute
	public int numberOfCards = CardTDG.getNumberOfCardsPerDeck();

	public UploadDeckFormCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			checkIfLoggedIn(NOT_LOGGED_IN);
			numberOfDecks = DeckInputMapper.findByPlayer(getUserId()).size();
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
