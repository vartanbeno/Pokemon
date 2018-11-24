package dom.model.attachedenergy;

import org.dsrg.soenea.domain.interf.IDomainObject;

import dom.model.bench.IBench;
import dom.model.game.IGame;
import dom.model.user.IUser;

public interface IAttachedEnergy extends IDomainObject<Long> {
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public long getGameVersion();
	public void setGameVersion(long gameVersion);
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public IBench getEnergyCard();
	public void setEnergyCard(IBench energyCard);
	
	public IBench getPokemonCard();
	public void setPokemonCard(IBench pokemonCard);
	
}
