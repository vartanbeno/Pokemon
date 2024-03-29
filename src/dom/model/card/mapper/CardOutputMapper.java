package dom.model.card.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.card.Card;
import dom.model.card.tdg.CardTDG;

public class CardOutputMapper extends GenericOutputMapper<Long, Card> {
	
	@Override
	public void insert(Card card) throws MapperException {
		try {
			CardTDG.insert(card.getId(), card.getVersion(), card.getDeck(), card.getType(), card.getName(), card.getBasic());
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Card card) throws MapperException {
		try {
			int count = CardTDG.update(card.getId(), card.getVersion(), card.getType(), card.getName(), card.getBasic());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update card with id: %d.", card.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Card card) throws MapperException {
		try {
			int count = CardTDG.delete(card.getId(), card.getVersion());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete card with id: %d.", card.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

}
