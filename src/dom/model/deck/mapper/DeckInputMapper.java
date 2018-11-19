package dom.model.deck.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.card.ICard;
import dom.model.card.mapper.CardInputMapper;
import dom.model.deck.Deck;
import dom.model.deck.DeckFactory;
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
		
		Deck deck = getFromIdentityMap(id);
		if (deck != null) return deck;
		
		ResultSet rs = DeckFinder.findById(id);
		
		deck = rs.next() ? buildDeck(rs) : null;
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
		
		return DeckFactory.createClean(rs.getLong("id"), rs.getLong("version"), player, cards);
		
	}
	
	public static List<IDeck> buildDecks(ResultSet rs) throws SQLException {
		
		List<IDeck> decks = new ArrayList<IDeck>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			Deck deck = getFromIdentityMap(id);
			
			if (deck == null) deck = buildDeck(rs);
			
			decks.add(deck);
			
		}
		
		rs.close();
		
		return decks;
		
	}
	
	public static Deck getFromIdentityMap(long id) {
		
		Deck deck = null;
		
		try {
			deck = IdentityMap.get(id, Deck.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return deck;
		
	}

}
