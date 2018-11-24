package dom.model.bench.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.bench.Bench;
import dom.model.bench.tdg.BenchTDG;

public class BenchOutputMapper extends GenericOutputMapper<Long, Bench> {

	@Override
	public void insert(Bench benchCard) throws MapperException {
		try {
			insertStatic(benchCard);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Bench benchCard) throws MapperException {
		try {
			updateStatic(benchCard);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Bench benchCard) throws MapperException {
		try {
			deleteStatic(benchCard);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(Bench benchCard) throws SQLException, LostUpdateException {
		BenchTDG.insert(
				benchCard.getId(),
				benchCard.getVersion(),
				benchCard.getGame().getId(),
				benchCard.getPlayer().getId(),
				benchCard.getDeck(),
				benchCard.getCard().getId()
		);
	}
	
	public static void updateStatic(Bench benchCard) throws SQLException, LostUpdateException {
		int count = BenchTDG.update(benchCard.getId(), benchCard.getVersion(), benchCard.getCard().getId());
		if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update card in play with id: %d.", benchCard.getId()));
	}
	
	public static void deleteStatic(Bench benchCard) throws SQLException, LostUpdateException {
		int count = BenchTDG.delete(benchCard.getId(), benchCard.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete card in play with id: %d.", benchCard.getId()));
	}

}
