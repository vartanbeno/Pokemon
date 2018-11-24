package dom.model.attachedenergy;

import org.dsrg.soenea.domain.DomainObject;

import dom.model.bench.IBench;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class AttachedEnergy extends DomainObject<Long> implements IAttachedEnergy {
	
	private IGame game;
	private long gameVersion;
	private IUser player;
	private IBench energyCard;
	private IBench pokemonCard;
	
	public AttachedEnergy(
			long id, long version, IGame game, long gameVersion, IUser player, IBench energyCard, IBench pokemonCard
	) {
		super(id, version);
		this.game = game;
		this.gameVersion = gameVersion;
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
	public long getGameVersion() {
		return gameVersion;
	}

	@Override
	public void setGameVersion(long gameVersion) {
		this.gameVersion = gameVersion;
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
	public IBench getEnergyCard() {
		return energyCard;
	}

	@Override
	public void setEnergyCard(IBench energyCard) {
		this.energyCard = energyCard;
	}

	@Override
	public IBench getPokemonCard() {
		return pokemonCard;
	}

	@Override
	public void setPokemonCard(IBench pokemonCard) {
		this.pokemonCard = pokemonCard;
	}
	
}
