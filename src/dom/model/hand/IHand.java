package dom.model.hand;

import org.dsrg.soenea.domain.interf.IDomainObject;

import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public interface IHand extends IDomainObject<Long> {
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public long getDeck();
	public void setDeck(long deck);
	
	public ICard getCard();
	public void setCard(ICard card);
	
}
