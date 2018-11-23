package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.card.CardType;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.CardInPlayFactory;
import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayInputMapper;
import dom.model.game.Game;
import dom.model.game.GameFactory;
import dom.model.game.GameStatus;

public class PlayCardCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String NOT_YOUR_GAME = "You are not part of this game.";
	private static final String BENCH_IS_FULL = "Your bench is full. It already has 5 Pokemon.";
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot bench it.";
	private static final String GAME_STOPPED = "This game is over. You cannot continue playing.";
	private static final String EMPTY_HAND = "You do not have any cards in your hand.";
	private static final String NOT_YOUR_TURN = "It is not your turn yet.";
	
	private static final String POKEMON_BENCH_SUCCESS = "You have sent %s to the bench!";
	private static final String TRAINER_DISCARD_SUCCESS = "You have sent %s to the discard pile!";
	
	public PlayCardCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			long userId = getUserId();
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			Game game = getGame(gameId);
			
			if (userId != game.getChallenger().getId() && userId != game.getChallengee().getId())
				throw new CommandException(NOT_YOUR_GAME);
			if (game.getStatus() != GameStatus.ongoing.ordinal()) throw new CommandException(GAME_STOPPED);
			if (userId != game.getCurrentTurn()) throw new CommandException(NOT_YOUR_TURN);
			
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
			
			List<ICardInPlay> myHand = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
					game.getId(), getUserId(), CardStatus.hand.ordinal()
			);
			if (myHand.size() == 0) throw new CommandException(EMPTY_HAND);
			
			CardInPlay card;
			try {
				card = (CardInPlay) myHand.get(cardIndex);
			}
			catch (IndexOutOfBoundsException e) {
				throw new CommandException(NOT_IN_HAND);
			}
			
			if (card.getCard().getType().equals(CardType.p.name())) {
				
				List<ICardInPlay> myBench = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
						game.getId(), getUserId(), CardStatus.benched.ordinal()
				);
				if (myBench.size() >= 5) throw new CommandException(BENCH_IS_FULL);
				
				card.setStatus(CardStatus.benched.ordinal());
				this.message = String.format(POKEMON_BENCH_SUCCESS, card.getCard().getName());
				
			}
			else if (card.getCard().getType().equals(CardType.t.name())) {
				card.setStatus(CardStatus.discarded.ordinal());
				this.message = String.format(TRAINER_DISCARD_SUCCESS, card.getCard().getName());
			}
			else if (card.getCard().getType().equals(CardType.e.name())) {
				// TODO
				throw new CommandException("energy card");
			}
			
			GameFactory.registerDirty(game);
			CardInPlayFactory.registerDirty(card);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
