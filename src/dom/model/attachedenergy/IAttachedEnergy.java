package dom.model.attachedenergy;

import org.dsrg.soenea.domain.interf.IDomainObject;

import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public interface IAttachedEnergy extends IDomainObject<Long> {
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public long getGameTurn();
	public void setGameTurn(long gameTurn);
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public ICard getEnergyCard();
	public void setEnergyCard(ICard energyCard);
	
	public long getPokemonCard();
	public void setPokemonCard(long pokemonCard);
	
}
