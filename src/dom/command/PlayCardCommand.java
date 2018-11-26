package dom.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.attachedenergy.AttachedEnergyFactory;
import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.attachedenergy.mapper.AttachedEnergyInputMapper;
import dom.model.bench.BenchFactory;
import dom.model.bench.IBench;
import dom.model.bench.mapper.BenchInputMapper;
import dom.model.card.CardType;
import dom.model.discard.DiscardFactory;
import dom.model.game.Game;
import dom.model.hand.HandFactory;
import dom.model.hand.IHand;
import dom.model.hand.mapper.HandInputMapper;

public class PlayCardCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to play.";
	
	private static final String BENCH_IS_FULL = "Your bench is full. It already has 5 Pokemon.";
	private static final String NOT_IN_HAND = "That card is not in your hand. You cannot play it.";
	private static final String EMPTY_HAND = "You do not have any cards in your hand.";
	private static final String POKEMON_NOT_SPECIFIED_ENERGY = "You must specify a Pokemon (non-negative integer).";
	private static final String POKEMON_NOT_SPECIFIED_EVOLVE = "You must specify a Pokemon (non-negative integer) that can evolve into %s.";
	private static final String NOT_ON_BENCH = "That card is not on your bench. You cannot attach an energy to it.";
	private static final String ENERGY_ALREADY_PLAYED = "You have already played an energy card this turn.";
	private static final String EVOLVE_FAILURE = "%s cannot evolve into %s.";
	
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
			
			if (handCard.getCard().getType().equals(CardType.p.name()) || handCard.getCard().getType().equals(CardType.e.name())) {
				
				List<IBench> myBench = BenchInputMapper.findByGameAndPlayer(game.getId(), getUserId());
				
				/**
				 * Code taken from:
				 * https://stackoverflow.com/questions/35650974/create-list-of-object-from-another-using-java8-streams
				 */
				List<IBench> pokemonOnBench =
						myBench.stream().filter(card -> card.getCard().getType().equals(CardType.p.name())).collect(Collectors.toList());
				
				if (handCard.getCard().getType().equals(CardType.p.name())) {
					
					Integer pokemon = getPokemon();
					if (handCard.getCard().getBasic() != "" && pokemon != null) {
						
						IBench pokemonToEvolve = pokemonOnBench.get(pokemon);
						
						if (!handCard.getCard().getBasic().equals(pokemonToEvolve.getCard().getName())) {
							throw new CommandException(
									String.format(EVOLVE_FAILURE,
											pokemonToEvolve.getCard().getName(), handCard.getCard().getName()));
						}
						
						/**
						 * Delete card from hand.
						 * Replace card on bench with card from hand, keeping all attached energies.
						 * Place replaced card in discard pile.
						 */
						HandFactory.registerDeleted(handCard);
						BenchFactory.registerDirty(
								pokemonToEvolve.getId(),
								pokemonToEvolve.getVersion(),
								pokemonToEvolve.getGame(),
								pokemonToEvolve.getPlayer(),
								pokemonToEvolve.getDeck(),
								handCard.getCard(),
								pokemonToEvolve.getAttachedEnergyCards()
						);
						DiscardFactory.createNew(
								pokemonToEvolve.getGame(),
								pokemonToEvolve.getPlayer(),
								pokemonToEvolve.getDeck(),
								pokemonToEvolve.getCard()
						);
						
						this.message = String.format(EVOLVE_SUCCESS, pokemonToEvolve.getCard().getName(), handCard.getCard().getName());
						
					}
					else if (!handCard.getCard().getBasic().isEmpty() && pokemon == null) {
						throw new CommandException(String.format(POKEMON_NOT_SPECIFIED_EVOLVE, handCard.getCard().getName()));
					}
					else {
						
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
						
						if (pokemonOnBench.size() >= 5) throw new CommandException(BENCH_IS_FULL);
						
						this.message = String.format(POKEMON_BENCH_SUCCESS, handCard.getCard().getName(), pokemonOnBench.size() + 1);
						
					}
					
				}
				else {
					
					List<IAttachedEnergy> thisTurnEnergies =
							AttachedEnergyInputMapper.findByGameAndGameVersionAndPlayer(game.getId(), game.getVersion(), game.getCurrentTurn());
					
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
					HandFactory.registerDeleted(handCard);
					AttachedEnergyFactory.createNew(
							handCard.getGame(),
							handCard.getGame().getVersion(),
							handCard.getPlayer(),
							handCard.getCard(),
							pokemonCard.getId()
					);
					
					this.message = String.format(
							ATTACH_ENERGY_SUCCESS,
							handCard.getCard().getName(), pokemonCard.getCard().getName()
					);
					
				}
								
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
			
			// TODO should we increment game version?
//			GameFactory.registerDirty(game);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}
	
	private Integer getPokemon() {
		
		Integer pokemon = null;
		try {
			pokemon = helper.getInt("pokemon");
		}
		catch (NumberFormatException e) { }
		
		return pokemon;
	}

}
