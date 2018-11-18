package dom.model.cardinplay;

import org.dsrg.soenea.domain.DomainObject;

import dom.model.card.ICard;
import dom.model.deck.IDeck;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class CardInPlay extends DomainObject<Long> implements ICardInPlay {
	
	private IGame game;
	private IUser player;
	private IDeck deck;
	private ICard card;
	private int status;
	
	public CardInPlay(long id, long version, IGame game, IUser player, IDeck deck, ICard card, int status) {
		super(id, version);
		this.game = game;
		this.player = player;
		this.deck = deck;
		this.card = card;
		this.status = status;
	}

	@Override
	public IGame getGame() {
		return game;
	}

	@Override
	public void setGame(IGame game) {
		this.game = game;
	}

	@Override
	public IUser getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(IUser player) {
		this.player = player;
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
	public ICard getCard() {
		return card;
	}

	@Override
	public void setCard(ICard card) {
		this.card = card;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

}
