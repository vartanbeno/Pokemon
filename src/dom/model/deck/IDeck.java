package dom.model.deck;

import java.util.List;

import dom.model.card.ICard;
import dom.model.user.IUser;

public interface IDeck {
	
	public long getId();
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public List<ICard> getCards();
	public void setCards(List<ICard> cards);

}
