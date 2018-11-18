package dom.model.card;

import org.dsrg.soenea.domain.interf.IDomainObject;

public interface ICard extends IDomainObject<Long> {
	
	public long getDeck();
	public void setDeck(long deck);
	
	public String getType();
	public void setType(String type);
	
	public String getName();
	public void setName(String name);

}
