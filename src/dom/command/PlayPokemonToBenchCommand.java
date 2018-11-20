package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.uow.UoW;

import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.CardInPlayFactory;
import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayInputMapper;
import dom.model.game.Game;
import dom.model.game.GameFactory;
import dom.model.game.GameStatus;

public class PlayPokemonToBenchCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String CARD_INDEX_FORMAT = "You must provide a valid format for the card index (positive integer).";
	private static final String BENCH_IS_FULL = "Your bench is full. It already has 5 Pokemon.";
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot bench it.";
	private static final String NOT_A_POKEMON = "You can only bench cards of type 'p', i.e. Pokemon.";
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
	private static final String EMPTY_HAND = "You do not have any cards in your hand.";
	
	private static final String BENCH_SUCCESS = "You have sent %s to the bench!";
	
	@SetInRequestAttribute
	public String message;
	
	public PlayPokemonToBenchCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			Game game = getGame();
			if (game.getStatus() != GameStatus.ongoing.ordinal()) throw new CommandException(GAME_STOPPED);
			
			/**
			 * This is the card index.
			 * Example: let's say your hand is: [2, 5, 9, 12, 27]
			 * Going off of a zero-based index:
			 * If you choose card 0, you get 2.
			 * If you choose card 3, you get 12.
			 * If you choose card 10, you should get an error.
			 * etc.
			 */
			final int cardIndex;
			try {
				cardIndex = helper.getInt("card");
			}
			catch (NumberFormatException e) {
				throw new CommandException(CARD_INDEX_FORMAT);
			}
			
			List<ICardInPlay> myBench = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
					game.getId(), getUserId(), CardStatus.benched.ordinal()
			);
			if (myBench.size() >= 5) throw new CommandException(BENCH_IS_FULL);
			
			List<ICardInPlay> myHand = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
					game.getId(), getUserId(), CardStatus.hand.ordinal()
			);
			if (myHand.size() == 0) throw new CommandException(EMPTY_HAND);
			
			CardInPlay cardToBench;
			try {
				cardToBench = (CardInPlay) myHand.get(cardIndex);
			}
			catch (IndexOutOfBoundsException e) {
				throw new CommandException(NOT_IN_HAND);
			}
			
			if (!cardToBench.getCard().getType().equals("p"))
				throw new CommandException(NOT_A_POKEMON);
			
			cardToBench.setStatus(CardStatus.benched.ordinal());
			
			GameFactory.registerDirty(game);
			CardInPlayFactory.registerDirty(cardToBench);
			
			UoW.getCurrent().commit();
			
			this.message = String.format(BENCH_SUCCESS, cardToBench.getCard().getName());
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
