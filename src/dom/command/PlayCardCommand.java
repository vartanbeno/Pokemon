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
import dom.model.game.GameFactory;
import dom.model.game.IGame;
import dom.model.hand.HandFactory;
import dom.model.hand.IHand;
import dom.model.hand.mapper.HandInputMapper;

public class PlayCardCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot play it.";
	private static final String POKEMON_NOT_SPECIFIED_ENERGY = "You must specify a Pokemon (non-negative integer) to attach the energy to.";
	private static final String POKEMON_NOT_SPECIFIED_EVOLVE = "You must specify a basic Pokemon (non-negative integer) that can evolve into %s.";
	private static final String NOT_ON_BENCH = "That Pokemon is not on your bench. You cannot attach an energy to it.";
	private static final String ENERGY_ALREADY_PLAYED = "You have already played an energy card this turn.";
	private static final String EVOLVE_FAILURE = "%s cannot evolve into %s.";
	private static final String CANNOT_PLAY_TRAINER = "You cannot play a trainer onto a Pokemon.";
	
	private static final String POKEMON_BENCH_SUCCESS = "You have sent %s to the bench!";
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
			long gameVersion = getVersion();
			
			IGame game = getGame(gameId);
			
			checkIfImPartOfGame(game);
			checkIfGameHasEnded(game);
			checkIfItsMyTurn(game);
			
			IHand handCard = getCardFromHand(game);
			
			if (handCard.getCard().getType().equals(CardType.p.name()) || handCard.getCard().getType().equals(CardType.e.name())) {
				
				if (handCard.getCard().getType().equals(CardType.p.name())) {
					playPokemon(game, handCard);
				}
				else {
					playEnergy(game, handCard);
				}
				
			}
			else if (handCard.getCard().getType().equals(CardType.t.name())) {
				playTrainer(handCard);
			}
			
			/**
			 * This increments the game version by 1.
			 * Doesn't modify the current_turn or turn attributes.
			 */
			GameFactory.registerDirty(
					game.getId(),
					gameVersion,
					game.getChallenger(),
					game.getChallengee(),
					game.getChallengerDeck(),
					game.getChallengeeDeck(),
					game.getCurrentTurn(),
					game.getTurn(),
					game.getStatus()
			);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}
	
	private IHand getCardFromHand(IGame game) throws SQLException, CommandException {
		
		long cardId = Long.parseLong((String) helper.getRequestAttribute("card"));
		IHand handCard = HandInputMapper.findByGameAndPlayerAndCard(game.getId(), getUserId(), cardId);
		
		if (handCard == null) throw new CommandException(NOT_IN_HAND);
		
		return handCard;
		
	}
	
	private IBench getCardFromBench(IGame game, long cardId) throws SQLException, CommandException {
		
		IBench benchCard = BenchInputMapper.findByGameAndPlayerAndCard(game.getId(), getUserId(), cardId);
		
		if (benchCard == null) throw new CommandException(NOT_ON_BENCH);
		
		return benchCard;
		
	}
	
	private Long getBasic() {
		Long basic = null;
		try {
			basic = helper.getLong("basic");
		}
		catch (NumberFormatException e) { }
		
		return basic;
	}
	
	private Long getPokemon() {
		Long pokemon = null;
		try {
			pokemon = helper.getLong("pokemon");
		}
		catch (NumberFormatException e) { }
		
		return pokemon;
	}
	
	private void playPokemon(IGame game, IHand pokemonInHand)
			throws CommandException, MissingMappingException, MapperException, SQLException {
		
		Long basicPokemon = getBasic();
		
		if (!pokemonInHand.getCard().getBasic().isEmpty() && basicPokemon != null) {
			
			IBench pokemonOnBench = getCardFromBench(game, basicPokemon);
			
			if (!pokemonInHand.getCard().getBasic().equals(pokemonOnBench.getCard().getName())) {
				throw new CommandException(
						String.format(
								EVOLVE_FAILURE,
								pokemonOnBench.getCard().getName(), pokemonInHand.getCard().getName()
						)
				);
			}
			
			/**
			 * Delete card from hand.
			 * Replace card on bench with card from hand, keeping all attached energies.
			 * Place replaced card in discard pile.
			 */
			HandFactory.registerDeleted(
					pokemonInHand.getId(),
					pokemonInHand.getVersion(),
					pokemonInHand.getGame(),
					pokemonInHand.getPlayer(),
					pokemonInHand.getDeck(),
					pokemonInHand.getCard()
			);
			BenchFactory.registerDirty(
					pokemonOnBench.getId(),
					pokemonOnBench.getVersion(),
					pokemonOnBench.getGame(),
					pokemonOnBench.getPlayer(),
					pokemonOnBench.getDeck(),
					pokemonInHand.getCard(),
					pokemonOnBench.getCard(),
					pokemonOnBench.getAttachedEnergyCards()
			);
			DiscardFactory.createNew(
					pokemonOnBench.getGame(),
					pokemonOnBench.getPlayer(),
					pokemonOnBench.getDeck(),
					pokemonOnBench.getCard()
			);
			
			this.message = String.format(EVOLVE_SUCCESS, pokemonOnBench.getCard().getName(), pokemonInHand.getCard().getName());
			
		}
		else if (!pokemonInHand.getCard().getBasic().isEmpty() && basicPokemon == null) {
			throw new CommandException(String.format(POKEMON_NOT_SPECIFIED_EVOLVE, pokemonInHand.getCard().getName()));
		}
		else {
			
			/**
			 * Delete card from hand.
			 * Create card on bench.
			 * Its predecessor is itself, since it doesn't have a 'basic' type and doesn't replace anything on the bench.
			 */
			HandFactory.registerDeleted(
					pokemonInHand.getId(),
					pokemonInHand.getVersion(),
					pokemonInHand.getGame(),
					pokemonInHand.getPlayer(),
					pokemonInHand.getDeck(),
					pokemonInHand.getCard()
			);
			BenchFactory.createNew(
					pokemonInHand.getGame(),
					pokemonInHand.getPlayer(),
					pokemonInHand.getDeck(),
					pokemonInHand.getCard(),
					pokemonInHand.getCard(),
					new ArrayList<IAttachedEnergy>()
			);
			
			this.message = String.format(POKEMON_BENCH_SUCCESS, pokemonInHand.getCard().getName());
			
		}
		
	}
	
	private void playEnergy(IGame game, IHand energyInHand)
			throws SQLException, CommandException, MissingMappingException, MapperException {
		
		List<IAttachedEnergy> thisTurnEnergies =
				AttachedEnergyInputMapper.findByGameAndGameTurnAndPlayer(game.getId(), game.getTurn(), game.getCurrentTurn());
		
		if (thisTurnEnergies.size() > 0) {
			throw new CommandException(ENERGY_ALREADY_PLAYED);
		}
		
		Long pokemon = getPokemon();
		if (pokemon == null) throw new CommandException(POKEMON_NOT_SPECIFIED_ENERGY);
		
		IBench pokemonCard = getCardFromBench(game, pokemon);
		
		/**
		 * Delete card from hand.
		 * Attach it to a Pokemon on the bench.
		 */
		HandFactory.registerDeleted(
				energyInHand.getId(),
				energyInHand.getVersion(),
				energyInHand.getGame(),
				energyInHand.getPlayer(),
				energyInHand.getDeck(),
				energyInHand.getCard()
		);
		AttachedEnergyFactory.createNew(
				energyInHand.getGame(),
				energyInHand.getGame().getTurn(),
				energyInHand.getPlayer(),
				energyInHand.getCard(),
				pokemonCard.getId()
		);
		
		this.message = String.format(
				ATTACH_ENERGY_SUCCESS,
				energyInHand.getCard().getName(), pokemonCard.getCard().getName()
		);
		
	}
	
	private void playTrainer(IHand trainerCard)
			throws MissingMappingException, MapperException, SQLException, CommandException {
		
		Long pokemon = getPokemon();
		if (pokemon != null) throw new CommandException(CANNOT_PLAY_TRAINER);
		
		/**
		 * Delete card from hand.
		 * Create card in discard pile.
		 */
		HandFactory.registerDeleted(
				trainerCard.getId(),
				trainerCard.getVersion(),
				trainerCard.getGame(),
				trainerCard.getPlayer(),
				trainerCard.getDeck(),
				trainerCard.getCard()
		);
		DiscardFactory.createNew(
				trainerCard.getGame(),
				trainerCard.getPlayer(),
				trainerCard.getDeck(),
				trainerCard.getCard()
		);
		
		this.message = String.format(TRAINER_DISCARD_SUCCESS, trainerCard.getCard().getName());
		
	}

}
