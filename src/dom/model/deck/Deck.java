package dom.model.deck;

import java.util.List;

import dom.model.card.ICard;
import dom.model.user.IUser;

public class Deck implements IDeck {
	
	private long id;
	private IUser player;
	private List<ICard> cards;
	
	public Deck(long id, IUser player, List<ICard> cards) {
		this.id = id;
		this.player = player;
		this.cards = cards;
	}

	@Override
	public long getId() {
		return id;
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
	public List<ICard> getCards() {
		return cards;
	}
	
	@Override
	public void setCards(List<ICard> cards) {
		this.cards = cards;
	}

}
