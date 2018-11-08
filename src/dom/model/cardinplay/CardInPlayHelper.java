package dom.model.cardinplay;

import dom.model.card.CardHelper;
import dom.model.deck.DeckHelper;
import dom.model.game.GameHelper;
import dom.model.user.UserHelper;

public class CardInPlayHelper {
	
	private long id;
	private GameHelper game;
	private UserHelper player;
	private DeckHelper deck;
	private CardHelper card;
	private int status;
	
	public CardInPlayHelper(long id, GameHelper game, UserHelper player, DeckHelper deck, CardHelper card, int status) {
		this.id = id;
		this.game = game;
		this.player = player;
		this.deck = deck;
		this.card = card;
		this.status = status;
	}
	
	public long getId() {
		return id;
	}
	
	public GameHelper getGame() {
		return game;
	}
	
	public UserHelper getPlayer() {
		return player;
	}
	
	public DeckHelper getDeck() {
		return deck;
	}
	
	public CardHelper getCard() {
		return card;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
