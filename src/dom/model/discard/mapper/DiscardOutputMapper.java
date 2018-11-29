package dom.model.discard.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.discard.Discard;
import dom.model.discard.tdg.DiscardTDG;

public class DiscardOutputMapper extends GenericOutputMapper<Long, Discard> {

	@Override
	public void insert(Discard discardCard) throws MapperException {
		try {
			DiscardTDG.insert(
					discardCard.getId(),
					discardCard.getVersion(),
					discardCard.getGame().getId(),
					discardCard.getPlayer().getId(),
					discardCard.getDeck(),
					discardCard.getCard().getId()
			);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Discard discardCard) throws MapperException {
		try {
			int count = DiscardTDG.update(discardCard.getId(), discardCard.getVersion(), discardCard.getCard().getId());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update card in play with id: %d.", discardCard.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Discard discardCard) throws MapperException {
		try {
			int count = DiscardTDG.delete(discardCard.getId(), discardCard.getVersion());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete card in play with id: %d.", discardCard.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

}
