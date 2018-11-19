package dom.model.card.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.card.Card;
import dom.model.card.CardFactory;
import dom.model.card.ICard;
import dom.model.card.tdg.CardFinder;

public class CardInputMapper {
	
	public static Card findById(long id) throws SQLException {
		
		ResultSet rs = CardFinder.findById(id);
		
		Card card = rs.next() ? buildCard(rs) : null;
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
				rs.getString("name")
		);
		
	}
	
	public static List<ICard> buildCards(ResultSet rs) throws SQLException {
		
		List<ICard> cards = new ArrayList<ICard>();
		
		while (rs.next()) {
			
			Card card = buildCard(rs);
			cards.add(card);
			
		}
		
		rs.close();
		
		return cards;
		
	}

}
