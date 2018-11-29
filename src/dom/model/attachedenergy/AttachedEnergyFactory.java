package dom.model.attachedenergy;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.attachedenergy.tdg.AttachedEnergyTDG;
import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class AttachedEnergyFactory {
	
	public static AttachedEnergy createNew(
			IGame game, long gameTurn, IUser player, ICard energyCard, long pokemonCard
	) throws MissingMappingException, MapperException, SQLException {
		
		AttachedEnergy attachedEnergy = new AttachedEnergy(AttachedEnergyTDG.getMaxId(), 1, game, gameTurn, player, energyCard, pokemonCard);
		UoW.getCurrent().registerNew(attachedEnergy);
		
		return attachedEnergy;
		
	}
	
	public static AttachedEnergy createClean(
			long id, long version, IGame game, long gameTurn, IUser player, ICard energyCard, long pokemonCard
	) {
		
		AttachedEnergy attachedEnergy = new AttachedEnergy(id, version, game, gameTurn, player, energyCard, pokemonCard);
		UoW.getCurrent().registerClean(attachedEnergy);
		
		return attachedEnergy;
		
	}

}
