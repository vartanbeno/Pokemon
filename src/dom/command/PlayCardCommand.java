package dom.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.uow.MissingMappingException;

import dom.model.attachedenergy.AttachedEnergyFactory;
import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.attachedenergy.mapper.AttachedEnergyInputMapper;
import dom.model.bench.BenchFactory;
import dom.model.bench.IBench;
import dom.model.bench.mapper.BenchInputMapper;
import dom.model.card.CardType;
import dom.model.discard.DiscardFactory;
import dom.model.game.Game;
import dom.model.game.GameFactory;
import dom.model.game.IGame;
import dom.model.hand.HandFactory;
import dom.model.hand.IHand;
import dom.model.hand.mapper.HandInputMapper;

public class PlayCardCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot play it.";
	private static final String EMPTY_HAND = "You do not have any cards in your hand.";
	private static final String POKEMON_NOT_SPECIFIED_ENERGY = "You must specify a Pokemon (non-negative integer) to attach the energy to.";
	private static final String POKEMON_NOT_SPECIFIED_EVOLVE = "You must specify a Pokemon (non-negative integer) that can evolve into %s.";
	private static final String NOT_ON_BENCH = "That card is not on your bench. You cannot attach an energy to it.";
	private static final String ENERGY_ALREADY_PLAYED = "You have already played an energy card this turn.";
	private static final String EVOLVE_FAILURE = "%s cannot evolve into %s.";
	private static final String CANNOT_PLAY_TRAINER = "You cannot play a trainer onto a Pokemon.";
	
	private static final String POKEMON_BENCH_SUCCESS = "You have sent %s to the bench! You now have %d Pokemon on your bench.";
	private static final String EVOLVE_SUCCESS = "You have successfully evolved %s into %s!";
	private static final String ATTACH_ENERGY_SUCCESS = "You have successfully attached a %s energy card to %s!";
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
			
			IHand handCard = getCardFromHand(game);
			
			if (handCard.getCard().getType().equals(CardType.p.name()) || handCard.getCard().getType().equals(CardType.e.name())) {
				
				List<IBench> myBench = BenchInputMapper.findByGameAndPlayer(game.getId(), getUserId());
				
				if (handCard.getCard().getType().equals(CardType.p.name())) {
					playPokemon(handCard, myBench);
				}
				else {
					playEnergy(game, handCard, myBench);
				}
							
			}
			else if (handCard.getCard().getType().equals(CardType.t.name())) {
				playTrainer(handCard);
			}
			
			/**
			 * This increments the game version by 1.
			 * Doesn't modify the current_turn or turn attributes.
			 */
			GameFactory.registerDirty(game);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}
	
	private IHand getCardFromHand(IGame game) throws SQLException, CommandException {
		
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
		return handCard;
	}
	
	private Integer getPokemon() {
		Integer pokemon = null;
		try {
			pokemon = helper.getInt("pokemon");
		}
		catch (NumberFormatException e) { }
		
		return pokemon;
	}
	
	private void playPokemon(IHand pokemonCard, List<IBench> pokemonOnBench)
			throws CommandException, MissingMappingException, MapperException, SQLException {
		
		Integer pokemon = getPokemon();
		if (!pokemonCard.getCard().getBasic().isEmpty() && pokemon != null) {
			
			IBench pokemonToEvolve = pokemonOnBench.get(pokemon);
			
			if (!pokemonCard.getCard().getBasic().equals(pokemonToEvolve.getCard().getName())) {
				throw new CommandException(
						String.format(EVOLVE_FAILURE,
								pokemonToEvolve.getCard().getName(), pokemonCard.getCard().getName()));
			}
			
			/**
			 * Delete card from hand.
			 * Replace card on bench with card from hand, keeping all attached energies.
			 * Place replaced card in discard pile.
			 */
			HandFactory.registerDeleted(pokemonCard);
			BenchFactory.registerDirty(
					pokemonToEvolve.getId(),
					pokemonToEvolve.getVersion(),
					pokemonToEvolve.getGame(),
					pokemonToEvolve.getPlayer(),
					pokemonToEvolve.getDeck(),
					pokemonCard.getCard(),
					pokemonToEvolve.getCard(),
					pokemonToEvolve.getAttachedEnergyCards()
			);
			DiscardFactory.createNew(
					pokemonToEvolve.getGame(),
					pokemonToEvolve.getPlayer(),
					pokemonToEvolve.getDeck(),
					pokemonToEvolve.getCard()
			);
			
			this.message = String.format(EVOLVE_SUCCESS, pokemonToEvolve.getCard().getName(), pokemonCard.getCard().getName());
			
		}
		else if (!pokemonCard.getCard().getBasic().isEmpty() && pokemon == null) {
			throw new CommandException(String.format(POKEMON_NOT_SPECIFIED_EVOLVE, pokemonCard.getCard().getName()));
		}
		else {
			
			/**
			 * Delete card from hand.
			 * Create card on bench.
			 * Its predecessor is itself, since it doesn't have a 'basic' type and doesn't replace anything on the bench.
			 */
			HandFactory.registerDeleted(pokemonCard);
			BenchFactory.createNew(
					pokemonCard.getGame(),
					pokemonCard.getPlayer(),
					pokemonCard.getDeck(),
					pokemonCard.getCard(),
					pokemonCard.getCard(),
					new ArrayList<IAttachedEnergy>()
			);
			
			this.message = String.format(POKEMON_BENCH_SUCCESS, pokemonCard.getCard().getName(), pokemonOnBench.size() + 1);
			
		}
		
	}
	
	private void playEnergy(IGame game, IHand energyCard, List<IBench> pokemonOnBench)
			throws SQLException, CommandException, MissingMappingException, MapperException {
		
		List<IAttachedEnergy> thisTurnEnergies =
				AttachedEnergyInputMapper.findByGameAndGameTurnAndPlayer(game.getId(), game.getTurn(), game.getCurrentTurn());
		
		if (thisTurnEnergies.size() > 0) {
			throw new CommandException(ENERGY_ALREADY_PLAYED);
		}
		
		Integer pokemon = getPokemon();
		if (pokemon == null) throw new CommandException(POKEMON_NOT_SPECIFIED_ENERGY);
		
		IBench pokemonCard = null;
		try {
			pokemonCard = pokemonOnBench.get(pokemon);
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new CommandException(NOT_ON_BENCH);
		}
		
		/**
		 * Delete card from hand.
		 * Attach it to a Pokemon on the bench.
		 */
		HandFactory.registerDeleted(energyCard);
		AttachedEnergyFactory.createNew(
				energyCard.getGame(),
				energyCard.getGame().getTurn(),
				energyCard.getPlayer(),
				energyCard.getCard(),
				pokemonCard.getId()
		);
		
		this.message = String.format(
				ATTACH_ENERGY_SUCCESS,
				energyCard.getCard().getName(), pokemonCard.getCard().getName()
		);
		
	}
	
	private void playTrainer(IHand trainerCard)
			throws MissingMappingException, MapperException, SQLException, CommandException {
		
		Integer pokemon = getPokemon();
		if (pokemon != null) throw new CommandException(CANNOT_PLAY_TRAINER);
		
		/**
		 * Delete card from hand.
		 * Create card in discard pile.
		 */
		HandFactory.registerDeleted(trainerCard);
		DiscardFactory.createNew(
				trainerCard.getGame(),
				trainerCard.getPlayer(),
				trainerCard.getDeck(),
				trainerCard.getCard()
		);
		
		this.message = String.format(TRAINER_DISCARD_SUCCESS, trainerCard.getCard().getName());
		
	}

}
