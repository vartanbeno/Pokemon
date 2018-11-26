package dom.model.card;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.card.tdg.CardTDG;

public class CardFactory {
	
	public static Card createNew(long deck, String type, String name, String basic)
			throws MissingMappingException, MapperException, SQLException {
		
		Card card = new Card(CardTDG.getMaxId(), 1, deck, type, name, basic);
		UoW.getCurrent().registerNew(card);
		
		return card;
		
	}
	
	public static Card createClean(long id, long version, long deck, String type, String name, String basic) {
		
		Card card = new Card(id, version, deck, type, name, basic);
		UoW.getCurrent().registerClean(card);
		
		return card;
		
	}
	
	public static Card createClean(ICard card) {
		return createClean(card.getId(), card.getVersion(), card.getDeck(), card.getType(), card.getName(), card.getBasic());
	}

}
