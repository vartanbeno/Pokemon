package dom.model.cardinplay;

import dom.model.card.ICard;
import dom.model.deck.IDeck;
import dom.model.game.IGame;
import dom.model.user.IUser;

public interface ICardInPlay {
	
	public long getId();
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public IUser getPlayer();
	public void setPlayer(IUser player);
	
	public IDeck getDeck();
	public void setDeck(IDeck deck);
	
	public ICard getCard();
	public void setCard(ICard card);
	
	public int getStatus();
	public void setStatus(int status);

}
