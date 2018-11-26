package dom.model.bench;

import java.util.List;

import org.dsrg.soenea.domain.interf.IDomainObject;

import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public interface IBench extends IDomainObject<Long> {
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public long getDeck();
	public void setDeck(long deck);
	
	public ICard getCard();
	public void setCard(ICard card);
	
	public ICard getPredecessor();
	public void setPredecessor(ICard predecessor);
	
	public List<IAttachedEnergy> getAttachedEnergyCards();
	public void setAttachedEnergyCards(List<IAttachedEnergy> attachedEnergyCards);
	
}
