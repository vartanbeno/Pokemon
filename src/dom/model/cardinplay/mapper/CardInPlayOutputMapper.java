package dom.model.cardinplay.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.tdg.CardInPlayTDG;
import dom.model.game.Game;
import dom.model.game.mapper.GameOutputMapper;

public class CardInPlayOutputMapper extends GenericOutputMapper<Long, CardInPlay> {

	@Override
	public void insert(CardInPlay cardInPlay) throws MapperException {
		try {
			insertStatic(cardInPlay);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(CardInPlay cardInPlay) throws MapperException {
		try {
			updateStatic(cardInPlay);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(CardInPlay cardInPlay) throws MapperException {
		try {
			deleteStatic(cardInPlay);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(CardInPlay cardInPlay) throws SQLException, LostUpdateException {
		GameOutputMapper.updateVersionStatic((Game) cardInPlay.getGame());
		CardInPlayTDG.insert(
				cardInPlay.getId(),
				cardInPlay.getVersion(),
				cardInPlay.getGame().getId(),
				cardInPlay.getPlayer().getId(),
				cardInPlay.getDeck().getId(),
				cardInPlay.getCard().getId()
		);
	}
	
	public static void updateStatic(CardInPlay cardInPlay) throws SQLException, LostUpdateException {
		GameOutputMapper.updateVersionStatic((Game) cardInPlay.getGame());
		int count = CardInPlayTDG.update(cardInPlay.getId(), cardInPlay.getVersion(), cardInPlay.getStatus());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update card in play with id: %d.", cardInPlay.getId()));
	}
	
	public static void deleteStatic(CardInPlay cardInPlay) throws SQLException, LostUpdateException {
		GameOutputMapper.updateVersionStatic((Game) cardInPlay.getGame());
		int count = CardInPlayTDG.delete(cardInPlay.getId(), cardInPlay.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot delete card in play with id: %d.", cardInPlay.getId()));
	}

}
