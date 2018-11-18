package dom.model.card.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.card.Card;
import dom.model.card.ICard;
import dom.model.card.tdg.CardTDG;

public class CardMapper extends GenericOutputMapper<Long, Card> {
	
	@Override
	public void insert(Card card) throws MapperException {
		try {
			insertStatic(card);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Card card) throws MapperException {
		try {
			updateStatic(card);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Card card) throws MapperException {
		try {
			deleteStatic(card);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(Card card) throws SQLException {
		CardTDG.insert(card.getId(), card.getVersion(), card.getDeck(), card.getType(), card.getName());
	}
	
	public static void updateStatic(Card card) throws SQLException, LostUpdateException {
		int count = CardTDG.update(card.getType(), card.getName(), card.getId(), card.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update card with id: %d.", card.getId()));
	}
	
	public static void deleteStatic(Card card) throws SQLException, LostUpdateException {
		int count = CardTDG.delete(card.getId(), card.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot delete card with id: %d.", card.getId()));
	}
	
	public static void deleteDeck(long deck) throws SQLException {
		CardTDG.deleteAllCardsInDeck(deck);
	}
	
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
	
	public static Card buildCard(ResultSet rs) throws SQLException {
		
		return new Card(
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
