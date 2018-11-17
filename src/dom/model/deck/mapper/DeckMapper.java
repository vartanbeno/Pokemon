package dom.model.deck.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.card.Card;
import dom.model.card.ICard;
import dom.model.card.mapper.CardMapper;
import dom.model.card.tdg.CardTDG;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.deck.tdg.DeckTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserMapper;
import dom.model.user.tdg.UserTDG;

public class DeckMapper {
	
	public static List<IDeck> findAll() throws SQLException {
		
		ResultSet rs = DeckTDG.findAll();
		
		List<IDeck> decks = buildDecks(rs);
		rs.close();
		
		return decks;
		
	}
	
	public static Deck findById(long id) throws SQLException {
		
		ResultSet rs = DeckTDG.findById(id);
		
		Deck deck = rs.next() ? buildDeck(rs) : null;
		rs.close();
		
		return deck;
		
	}
	
	public static List<IDeck> findByPlayer(long player) throws SQLException {
		
		ResultSet rs = DeckTDG.findByPlayer(player);
		
		List<IDeck> decks = buildDecks(rs);
		rs.close();
		
		return decks;
		
	}
	
	public static void insert(Deck deck) throws SQLException {
		
		DeckTDG.insert(deck.getId(), deck.getPlayer().getId());
		
		for (ICard card : deck.getCards()) {
			CardMapper.insert((Card) card);
		}
		
	}
	
	public static void delete(Deck deck) throws SQLException {
		
		for (ICard card : deck.getCards()) {
			CardMapper.delete((Card) card);
		}
		
		DeckTDG.delete(deck.getId());
		
	}
	
	public static Deck buildDeck(ResultSet rs) throws SQLException {
		
		ResultSet playerRS = UserTDG.findById(rs.getLong("player"));
		User player = playerRS.next() ? UserMapper.buildUser(playerRS) : null;
		playerRS.close();
		
		ResultSet cardsRS = CardTDG.findByDeck(rs.getLong("id"));
		List<ICard> cards = CardMapper.buildCards(cardsRS);
		cardsRS.close();
		
		return new Deck(rs.getLong("id"), player, cards);
		
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
