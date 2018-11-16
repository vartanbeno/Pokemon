package dom.model.card.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.card.Card;
import dom.model.card.ICard;
import dom.model.card.tdg.CardTDG;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.deck.tdg.DeckTDG;

public class CardMapper {
	
	public static Card findById(long id) throws SQLException {
		
		ResultSet rs = CardTDG.findById(id);
		
		Card card = rs.next() ? buildCard(rs) : null;
		rs.close();
		
		return card;
		
	}
	
	public static List<ICard> findByDeck(long deck) throws SQLException {
		
		ResultSet rs = CardTDG.findByDeck(deck);
		
		List<ICard> cards = buildCards(rs);
		rs.close();
		
		return cards;
		
	}
	
	public static void insert(Card card) throws SQLException {
		CardTDG.insert(card.getId(), card.getDeck().getId(), card.getType(), card.getName());
	}
	
	public static void delete(Card card) throws SQLException {
		CardTDG.delete(card.getId());
	}
	
	public static void deleteDeck(long deck) throws SQLException {
		CardTDG.deleteAllCardsInDeck(deck);
	}
	
	public static Card buildCard(ResultSet rs) throws SQLException {
		
		ResultSet deckRS = DeckTDG.findById(rs.getLong("id"));
		Deck deck = deckRS.next() ? DeckMapper.buildDeck(deckRS) : null;
		deckRS.close();
		
		return new Card(rs.getLong("id"), deck, rs.getString("type"), rs.getString("name"));
		
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
