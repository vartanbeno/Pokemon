package dom.model.deck;

import java.util.List;

import dom.model.card.CardHelper;
import dom.model.user.UserHelper;

public class DeckWithCardsHelper {
	
	private long id;
	private UserHelper player;
	private List<CardHelper> cards;
	
	public DeckWithCardsHelper(long id, UserHelper player, List<CardHelper> cards) {
		this.id = id;
		this.player = player;
		this.cards = cards;
	}

	public long getId() {
		return id;
	}

	public UserHelper getPlayer() {
		return player;
	}
	
	public List<CardHelper> getCards() {
		return cards;
	}
	
}
