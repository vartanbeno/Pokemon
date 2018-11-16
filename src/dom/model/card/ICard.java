package dom.model.card;

public interface ICard {
	
	public long getId();
	
	public long getDeck();
	public void setDeck(long deck);
	
	public String getType();
	public void setType(String type);
	
	public String getName();
	public void setName(String name);

}
