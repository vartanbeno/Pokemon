package dom.model.attachedenergy;

import org.dsrg.soenea.domain.interf.IDomainObject;

import dom.model.cardinplay.ICardInPlay;
import dom.model.game.IGame;
import dom.model.user.IUser;

public interface IAttachedEnergy extends IDomainObject<Long> {
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public long getGameVersion();
	public void setGameVersion(long gameVersion);
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public ICardInPlay getEnergyCard();
	public void setEnergyCard(ICardInPlay energyCard);
	
	public ICardInPlay getPokemonCard();
	public void setPokemonCard(ICardInPlay pokemonCard);
	
}
