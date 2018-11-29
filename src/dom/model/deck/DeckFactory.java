package dom.model.deck;

import java.sql.SQLException;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.card.ICard;
import dom.model.deck.tdg.DeckTDG;
import dom.model.user.IUser;

public class DeckFactory {
	
	public static Deck createNew(IUser player, List<ICard> cards)
			throws MissingMappingException, MapperException, SQLException {
		
		Deck deck = new Deck(DeckTDG.getMaxId(), 1, player, cards);
		UoW.getCurrent().registerNew(deck);
		
		return deck;
		
	}
	
	public static Deck createClean(long id, long version, IUser player, List<ICard> cards) {
		
		Deck deck = new Deck(id, version, player, cards);
		UoW.getCurrent().registerClean(deck);
		
		return deck;
		
	}

}
