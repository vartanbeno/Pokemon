package dom.model.card;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.card.mapper.CardInputMapper;

public class CardProxy extends DomainObjectProxy<Long, Card> implements ICard {

	public CardProxy(Long id) {
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
	public String getBasic() {
		return getInnerObject().getBasic();
	}

	@Override
	public void setBasic(String basic) {
		getInnerObject().setBasic(basic);
	}

	@Override
	protected Card getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return CardInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
