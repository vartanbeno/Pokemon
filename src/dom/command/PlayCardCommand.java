package dom.command;

import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.bench.BenchFactory;
import dom.model.bench.IBench;
import dom.model.bench.mapper.BenchInputMapper;
import dom.model.card.CardType;
import dom.model.discard.DiscardFactory;
import dom.model.game.Game;
import dom.model.game.GameFactory;
import dom.model.hand.HandFactory;
import dom.model.hand.IHand;
import dom.model.hand.mapper.HandInputMapper;

public class PlayCardCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String BENCH_IS_FULL = "Your bench is full. It already has 5 Pokemon.";
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot bench it.";
	private static final String EMPTY_HAND = "You do not have any cards in your hand.";
	
	private static final String POKEMON_BENCH_SUCCESS = "You have sent %s to the bench! You now have %d Pokemon on your bench.";
	private static final String TRAINER_DISCARD_SUCCESS = "You have sent %s to the discard pile!";
	
	public PlayCardCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			Game game = getGame(gameId);
			
			checkIfImPartOfGame(game);
			checkIfGameHasEnded(game);
			checkIfItsMyTurn(game);
			
			/**
			 * This is the card index.
			 * Example: let's say your hand is: [2, 5, 9, 12, 27]
			 * Going off of a zero-based index:
			 * If you choose card 0, you get 2.
			 * If you choose card 3, you get 12.
			 * If you choose card 10, you should get an error.
			 * etc.
			 */
			int cardIndex = Integer.parseInt((String) helper.getRequestAttribute("card"));
			
			List<IHand> myHand = HandInputMapper.findByGameAndPlayer(game.getId(), getUserId());
			if (myHand.size() == 0) throw new CommandException(EMPTY_HAND);
			
			IHand handCard;
			try {
				handCard = myHand.get(cardIndex);
			}
			catch (IndexOutOfBoundsException e) {
				throw new CommandException(NOT_IN_HAND);
			}
			
			if (handCard.getCard().getType().equals(CardType.p.name())) {
				
				List<IBench> myBench = BenchInputMapper.findByGameAndPlayer(game.getId(), getUserId());
				
				/**
				 * Only 5 Pokemon are allowed on the bench.
				 * 
				 * Code taken from:
				 * https://stackoverflow.com/questions/33503257/get-number-of-object-in-a-list-with-specific-matching-property-in-java
				 */
				long numberOfPokemonOnBench =
						myBench.stream().filter(card -> card.getCard().getType().equals(CardType.p.name())).count();
				
				if (numberOfPokemonOnBench >= 5) throw new CommandException(BENCH_IS_FULL);
				
				/**
				 * Delete card from hand.
				 * Create card on bench.
				 */
				HandFactory.registerDeleted(handCard);
				BenchFactory.createNew(
						handCard.getGame(),
						handCard.getPlayer(),
						handCard.getDeck(),
						handCard.getCard(),
						new ArrayList<IAttachedEnergy>()
				);
				
				this.message = String.format(POKEMON_BENCH_SUCCESS, handCard.getCard().getName(), numberOfPokemonOnBench + 1);
				
			}
			else if (handCard.getCard().getType().equals(CardType.t.name())) {
				
				/**
				 * Delete card from hand.
				 * Create card in discard pile.
				 */
				HandFactory.registerDeleted(handCard);
				DiscardFactory.createNew(
						handCard.getGame(),
						handCard.getPlayer(),
						handCard.getDeck(),
						handCard.getCard()
				);
				
				this.message = String.format(TRAINER_DISCARD_SUCCESS, handCard.getCard().getName());
			}
			else if (handCard.getCard().getType().equals(CardType.e.name())) {
				// TODO
				throw new CommandException("energy card");
			}
			
			GameFactory.registerDirty(game);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
