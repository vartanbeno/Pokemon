package dom.model.attachedenergy.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.attachedenergy.AttachedEnergy;
import dom.model.attachedenergy.tdg.AttachedEnergyTDG;

public class AttachedEnergyOutputMapper extends GenericOutputMapper<Long, AttachedEnergy> {

	@Override
	public void insert(AttachedEnergy attachedEnergy) throws MapperException {
		try {
			insertStatic(attachedEnergy);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(AttachedEnergy attachedEnergy) throws MapperException {
		try {
			updateStatic(attachedEnergy);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(AttachedEnergy attachedEnergy) throws MapperException {
		try {
			deleteStatic(attachedEnergy);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(AttachedEnergy attachedEnergy) throws SQLException {
		AttachedEnergyTDG.insert(
				attachedEnergy.getId(),
				attachedEnergy.getVersion(),
				attachedEnergy.getGame().getId(),
				attachedEnergy.getGameTurn(),
				attachedEnergy.getPlayer().getId(),
				attachedEnergy.getEnergyCard().getId(),
				attachedEnergy.getPokemonCard()
		);
	}
	
	public static void updateStatic(AttachedEnergy attachedEnergy) throws SQLException, LostUpdateException {
		int count = AttachedEnergyTDG.update(
				attachedEnergy.getId(),
				attachedEnergy.getVersion(),
				attachedEnergy.getEnergyCard().getId(),
				attachedEnergy.getPokemonCard()
		);
		if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update attached energy with id: %d.", attachedEnergy.getId()));
	}
	
	public static void deleteStatic(AttachedEnergy attachedEnergy) throws SQLException, LostUpdateException {
		int count = AttachedEnergyTDG.delete(attachedEnergy.getId(), attachedEnergy.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete attached energy with id: %d.", attachedEnergy.getId()));
	}
	
}
