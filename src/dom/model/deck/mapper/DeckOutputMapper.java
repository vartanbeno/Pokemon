package dom.model.deck.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.deck.Deck;
import dom.model.deck.tdg.DeckTDG;

public class DeckOutputMapper extends GenericOutputMapper<Long, Deck> {
	
	@Override
	public void insert(Deck deck) throws MapperException {
		try {
			DeckTDG.insert(deck.getId(), deck.getVersion(), deck.getPlayer().getId());
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Deck deck) throws MapperException {
		try {
			int count = DeckTDG.update(deck.getId(), deck.getVersion(), deck.getPlayer().getId());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update deck with id: %d.", deck.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Deck deck) throws MapperException {
		try {
			int count = DeckTDG.delete(deck.getId(), deck.getVersion());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete deck with id: %d.", deck.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

}
