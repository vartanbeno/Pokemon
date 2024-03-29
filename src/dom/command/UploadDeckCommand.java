package dom.command;

import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.CardFactory;
import dom.model.card.CardType;
import dom.model.card.ICard;
import dom.model.card.tdg.CardTDG;
import dom.model.deck.DeckFactory;
import dom.model.deck.IDeck;
import dom.model.user.IUser;
import dom.model.user.mapper.UserInputMapper;

public class UploadDeckCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to upload a deck.";
	
	private static final String CARDS_FAIL_MESSAGE = "You have %1$d card(s) in your deck. You must have %2$d.";
	private static final String FORMATTING_ERROR = "Make sure to adhere to the required formatting for each card.";
	private static final String CARD_TYPE_ERROR = "One or more of your cards had an invalid type. Please make sure it's one of e/p/t.";
	
	private static final String UPLOAD_SUCCESS = "You have successfully uploaded your deck.";
	
	public UploadDeckCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			IUser player = UserInputMapper.findById(getUserId());
			
			/**
			 * We delete carriage returns (\r) by replacing them with an empty String "".
			 * We make sure to trim the String, i.e. delete leftmost and rightmost spaces/newlines.
			 * Then, we get the cards by splitting by newline.
			 */
			String cardsArray[] = helper.getString("deck").replace("\r", "").trim().split("\n");
			
			int number = CardTDG.getNumberOfCardsPerDeck();
			if (cardsArray.length != number) throw new CommandException(String.format(CARDS_FAIL_MESSAGE, cardsArray.length, number));
			
			List<ICard> cards = new ArrayList<ICard>();
			IDeck deck = DeckFactory.createNew(player, cards);
			
			for (String cardString : cardsArray) {
				
				String[] cardInfo = cardString.replace("\"", "").split(" ");
				
				String type = "";
				String name = "";
				String basic = "";
				
				try {
					type = cardInfo[0];
					name = cardInfo[1];
					try {
						basic = cardInfo[2];
					}
					catch (IndexOutOfBoundsException e) {
						// don't do anything
					}
				}
				catch (IndexOutOfBoundsException e) {
					throw new CommandException(FORMATTING_ERROR);
				}
				
				if (!type.equals(CardType.e.name()) && !type.equals(CardType.p.name()) && !type.equals(CardType.t.name())) {
					throw new CommandException(CARD_TYPE_ERROR);
				}
				
				ICard card = CardFactory.createNew(deck.getId(), type, name, basic);
				cards.add(card);
				
			}
			
			this.message = UPLOAD_SUCCESS;
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
