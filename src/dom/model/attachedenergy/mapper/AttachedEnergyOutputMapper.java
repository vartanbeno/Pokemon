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
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(AttachedEnergy attachedEnergy) throws MapperException {
		try {
			int count = AttachedEnergyTDG.update(
					attachedEnergy.getId(),
					attachedEnergy.getVersion(),
					attachedEnergy.getEnergyCard().getId(),
					attachedEnergy.getPokemonCard()
			);
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update attached energy with id: %d.", attachedEnergy.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(AttachedEnergy attachedEnergy) throws MapperException {
		try {
			int count = AttachedEnergyTDG.delete(attachedEnergy.getId(), attachedEnergy.getVersion());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete attached energy with id: %d.", attachedEnergy.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
}
