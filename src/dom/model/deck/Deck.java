package dom.model.deck;

import java.util.List;

import org.dsrg.soenea.domain.DomainObject;

import dom.model.card.ICard;
import dom.model.user.IUser;

public class Deck extends DomainObject<Long> implements IDeck {
	
	private IUser player;
	private List<ICard> cards;
	
	public Deck(long id, long version, IUser player, List<ICard> cards) {
		super(id, version);
		this.player = player;
		this.cards = cards;
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
