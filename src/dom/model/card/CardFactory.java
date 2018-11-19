package dom.model.card;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

public class CardFactory {
	
	public static Card createNew(long id, long version, long deck, String type, String name)
			throws MissingMappingException, MapperException {
		
		Card card = new Card(id, version, deck, type, name);
		UoW.getCurrent().registerNew(card);
		
		return card;
		
	}
	
	public static Card createClean(long id, long version, long deck, String type, String name) {
		
		Card card = new Card(id, version, deck, type, name);
		UoW.getCurrent().registerClean(card);
		
		return card;
		
	}

}