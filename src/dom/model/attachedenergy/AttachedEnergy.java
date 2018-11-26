package dom.model.attachedenergy;

import org.dsrg.soenea.domain.DomainObject;

import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class AttachedEnergy extends DomainObject<Long> implements IAttachedEnergy {
	
	private IGame game;
	private long gameTurn;
	private IUser player;
	private ICard energyCard;
	private long pokemonCard;
	
	public AttachedEnergy(
			long id, long version, IGame game, long gameTurn, IUser player, ICard energyCard, long pokemonCard
	) {
		super(id, version);
		this.game = game;
		this.gameTurn = gameTurn;
		this.player = player;
		this.energyCard = energyCard;
		this.pokemonCard = pokemonCard;
	}

	@Override
	public IGame getGame() {
		return game;
	}

	@Override
	public void setGame(IGame game) {
		this.game = game;
	}

	@Override
	public long getGameTurn() {
		return gameTurn;
	}

	@Override
	public void setGameTurn(long gameTurn) {
		this.gameTurn = gameTurn;
	}

	@Override
	public IUser getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(IUser player) {
		this.player = player;
	}

	@Override
	public ICard getEnergyCard() {
		return energyCard;
	}

	@Override
	public void setEnergyCard(ICard energyCard) {
		this.energyCard = energyCard;
	}

	@Override
	public long getPokemonCard() {
		return pokemonCard;
	}

	@Override
	public void setPokemonCard(long pokemonCard) {
		this.pokemonCard = pokemonCard;
	}
	
}
