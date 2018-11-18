package dom.model.deck;

import java.util.List;

import org.dsrg.soenea.domain.interf.IDomainObject;

import dom.model.card.ICard;
import dom.model.user.IUser;

public interface IDeck extends IDomainObject<Long>{
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public List<ICard> getCards();
	public void setCards(List<ICard> cards);

}
