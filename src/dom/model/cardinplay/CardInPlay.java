package dom.model.cardinplay;

import dom.model.card.ICard;
import dom.model.deck.IDeck;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class CardInPlay implements ICardInPlay {
	
	private long id;
	private IGame game;
	private IUser player;
	private IDeck deck;
	private ICard card;
	private int status;
	
	public CardInPlay(long id, IGame game, IUser player, IDeck deck, ICard card, int status) {
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

	public IGame getGame() {
		return game;
	}

	public void setGame(IGame game) {
		this.game = game;
	}

	public IUser getPlayer() {
		return player;
	}

	public void setPlayer(IUser player) {
		this.player = player;
	}

	public IDeck getDeck() {
		return deck;
	}

	public void setDeck(IDeck deck) {
		this.deck = deck;
	}

	public ICard getCard() {
		return card;
	}

	public void setCard(ICard card) {
		this.card = card;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
