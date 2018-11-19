package dom.model.deck.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.card.ICard;
import dom.model.card.mapper.CardInputMapper;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.deck.tdg.DeckFinder;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class DeckInputMapper {
	
	public static List<IDeck> findAll() throws SQLException {
		
		ResultSet rs = DeckFinder.findAll();
		
		List<IDeck> decks = buildDecks(rs);
		rs.close();
		
		return decks;
		
	}
	
	public static Deck findById(long id) throws SQLException {
		
		ResultSet rs = DeckFinder.findById(id);
		
		Deck deck = rs.next() ? buildDeck(rs) : null;
		rs.close();
		
		return deck;
		
	}
	
	public static List<IDeck> findByPlayer(long player) throws SQLException {
		
		ResultSet rs = DeckFinder.findByPlayer(player);
		
		List<IDeck> decks = buildDecks(rs);
		rs.close();
		
		return decks;
		
	}
	
	public static Deck buildDeck(ResultSet rs) throws SQLException {
		
		User player = UserInputMapper.findById(rs.getLong("player"));
		List<ICard> cards = CardInputMapper.findByDeck(rs.getLong("id"));
		
		return new Deck(rs.getLong("id"), rs.getLong("version"), player, cards);
		
	}
	
	public static List<IDeck> buildDecks(ResultSet rs) throws SQLException {
		
		List<IDeck> decks = new ArrayList<IDeck>();
		
		while (rs.next()) {
			
			Deck deck = buildDeck(rs);
			decks.add(deck);
			
		}
		
		rs.close();
		
		return decks;
		
	}

}
