package dom.model.deck;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.card.ICard;
import dom.model.user.IUser;

public class DeckFactory {
	
	public static Deck createNew(long id, long version, IUser player, List<ICard> cards)
			throws MissingMappingException, MapperException {
		
		Deck deck = new Deck(id, version, player, cards);
		UoW.getCurrent().registerNew(deck);
		
		return deck;
		
	}
	
	public static Deck createNew(IDeck deck)
			throws MissingMappingException, MapperException {
		return createNew(deck.getId(), deck.getVersion(), deck.getPlayer(), deck.getCards());
	}
	
	public static Deck createClean(long id, long version, IUser player, List<ICard> cards) {
		
		Deck deck = new Deck(id, version, player, cards);
		UoW.getCurrent().registerClean(deck);
		
		return deck;
		
	}
	
	public static Deck createClean(IDeck deck) {
		return createClean(deck.getId(), deck.getVersion(), deck.getPlayer(), deck.getCards());
	}

}
