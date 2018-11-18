package dom.model.card;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.card.mapper.CardMapper;

public class CardProxy extends DomainObjectProxy<Long, Card> implements ICard {

	protected CardProxy(Long id) {
		super(id);
	}

	@Override
	public long getDeck() {
		return getInnerObject().getDeck();
	}

	@Override
	public void setDeck(long deck) {
		getInnerObject().setDeck(deck);
	}

	@Override
	public String getType() {
		return getInnerObject().getType();
	}

	@Override
	public void setType(String type) {
		getInnerObject().setType(type);
	}

	@Override
	public String getName() {
		return getInnerObject().getName();
	}

	@Override
	public void setName(String name) {
		getInnerObject().setName(name);
	}

	@Override
	protected Card getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return CardMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
