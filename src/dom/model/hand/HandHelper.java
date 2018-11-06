package dom.model.hand;

import dom.model.card.CardHelper;
import dom.model.deck.DeckHelper;
import dom.model.game.GameHelper;
import dom.model.user.UserHelper;

public class HandHelper {
	
	private long id;
	private GameHelper game;
	private UserHelper player;
	private DeckHelper deck;
	private CardHelper card;
	
	public HandHelper(long id, GameHelper game, UserHelper player, DeckHelper deck, CardHelper card) {
		super();
		this.id = id;
		this.game = game;
		this.player = player;
		this.deck = deck;
		this.card = card;
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
	
}
