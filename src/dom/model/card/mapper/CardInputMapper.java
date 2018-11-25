package dom.model.card.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.card.Card;
import dom.model.card.CardFactory;
import dom.model.card.ICard;
import dom.model.card.tdg.CardFinder;

public class CardInputMapper {
	
	public static Card findById(long id) throws SQLException {
		
		Card card = getFromIdentityMap(id);
		if (card != null) return card;
		
		ResultSet rs = CardFinder.findById(id);
		
		card = rs.next() ? buildCard(rs) : null;
		rs.close();
		
		return card;
		
	}
	
	public static List<ICard> findByDeck(long deck) throws SQLException {
		
		ResultSet rs = CardFinder.findByDeck(deck);
		
		List<ICard> cards = buildCards(rs);
		rs.close();
		
		return cards;
		
	}
	
	public static Card buildCard(ResultSet rs) throws SQLException {
		
		return CardFactory.createClean(
				rs.getLong("id"),
				rs.getLong("version"),
				rs.getLong("deck"),
				rs.getString("type"),
				rs.getString("name"),
				rs.getString("basic")
		);
		
	}
	
	public static List<ICard> buildCards(ResultSet rs) throws SQLException {
		
		List<ICard> cards = new ArrayList<ICard>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			Card card = getFromIdentityMap(id);
			
			if (card == null) card = buildCard(rs);
			
			cards.add(card);
			
		}
		
		rs.close();
		
		return cards;
		
	}
	
	public static Card getFromIdentityMap(long id) {
		
		Card card = null;
		
		try {
			card = IdentityMap.get(id, Card.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return card;
		
	}

}
