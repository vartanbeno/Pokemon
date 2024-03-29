package dom.model.card;

import org.dsrg.soenea.domain.DomainObject;

public class Card extends DomainObject<Long> implements ICard {
	
	private long deck;
	private String type;
	private String name;
	private String basic;
	
	public Card(long id, long version, long deck, String type, String name, String basic) {
		super(id, version);
		this.deck = deck;
		this.type = type;
		this.name = name;
		this.basic = basic;
	}

	@Override
	public long getDeck() {
		return deck;
	}

	@Override
	public void setDeck(long deck) {
		this.deck = deck;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getBasic() {
		return basic;
	}

	@Override
	public void setBasic(String basic) {
		this.basic = basic;
	}

}
