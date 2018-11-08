package dom.model.card;

import dom.model.deck.DeckHelper;

public class CardHelper {
	
	private long id;
	private DeckHelper deck;
	private String type;
	private String name;
	
	public CardHelper(Long id, DeckHelper deck, String type, String name) {
		this.id = id;
		this.deck = deck;
		this.type = type;
		this.name = name;
	}
	
	public long getId() {
		return id;
	}

	public DeckHelper getDeck() {
		return deck;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
