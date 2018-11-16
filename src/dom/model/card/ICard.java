package dom.model.card;

import dom.model.deck.IDeck;

public interface ICard {
	
	public long getId();
	
	public IDeck getDeck();
	public void setDeck(IDeck deck);
	
	public String getType();
	public void setType(String type);
	
	public String getName();
	public void setName(String name);

}
