package dom.model.card;

import dom.model.deck.IDeck;

public class Card implements ICard {
	
	private long id;
	private IDeck deck;
	private String type;
	private String name;
	
	public Card(long id, IDeck deck, String type, String name) {
		this.id = id;
		this.deck = deck;
		this.type = type;
		this.name = name;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public IDeck getDeck() {
		return deck;
	}

	@Override
	public void setDeck(IDeck deck) {
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

}
