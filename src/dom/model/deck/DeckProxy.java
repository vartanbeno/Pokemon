package dom.model.deck;

import java.util.List;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.card.ICard;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.user.IUser;

public class DeckProxy extends DomainObjectProxy<Long, Deck> implements IDeck {

	protected DeckProxy(Long id) {
		super(id);
	}

	@Override
	public IUser getPlayer() {
		return getInnerObject().getPlayer();
	}

	@Override
	public void setPlayer(IUser player) {
		getInnerObject().setPlayer(player);
	}

	@Override
	public List<ICard> getCards() {
		return getInnerObject().getCards();
	}

	@Override
	public void setCards(List<ICard> cards) {
		getInnerObject().setCards(cards);
	}

	@Override
	protected Deck getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return DeckInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
