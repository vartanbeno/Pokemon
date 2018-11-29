package dom.model.hand.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.hand.Hand;
import dom.model.hand.tdg.HandTDG;

public class HandOutputMapper extends GenericOutputMapper<Long, Hand> {

	@Override
	public void insert(Hand handCard) throws MapperException {
		try {
			HandTDG.insert(
					handCard.getId(),
					handCard.getVersion(),
					handCard.getGame().getId(),
					handCard.getPlayer().getId(),
					handCard.getDeck(),
					handCard.getCard().getId()
			);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Hand handCard) throws MapperException {
		try {
			int count = HandTDG.update(handCard.getId(), handCard.getVersion(), handCard.getCard().getId());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update card in play with id: %d.", handCard.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Hand handCard) throws MapperException {
		try {
			int count = HandTDG.delete(handCard.getId(), handCard.getVersion());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete card in play with id: %d.", handCard.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

}
