package dom.model.deck.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.card.Card;
import dom.model.card.ICard;
import dom.model.card.mapper.CardMapper;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.deck.tdg.DeckFinder;
import dom.model.deck.tdg.DeckTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class DeckMapper extends GenericOutputMapper<Long, Deck> {
	
	@Override
	public void insert(Deck deck) throws MapperException {
		try {
			insertStatic(deck);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Deck deck) throws MapperException {
		try {
			updateStatic(deck);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Deck deck) throws MapperException {
		try {
			deleteStatic(deck);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(Deck deck) throws SQLException {
		
		DeckTDG.insert(deck.getId(), deck.getVersion(), deck.getPlayer().getId());
		
		for (ICard card : deck.getCards()) {
			CardMapper.insertStatic((Card) card);
		}
		
	}
	
	public static void updateStatic(Deck deck) throws SQLException, LostUpdateException {
		int count = DeckTDG.update(deck.getId(), deck.getVersion(), deck.getPlayer().getId());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update deck with id: %d.", deck.getId()));
	}
	
	public static void deleteStatic(Deck deck) throws SQLException, LostUpdateException {
		int count = DeckTDG.delete(deck.getId(), deck.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot delete deck with id: %d.", deck.getId()));
		CardMapper.deleteDeck(deck.getId());
	}
	
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
		List<ICard> cards = CardMapper.findByDeck(rs.getLong("id"));
		
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
