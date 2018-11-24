package dom.model.attachedenergy;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.attachedenergy.tdg.AttachedEnergyTDG;
import dom.model.cardinplay.ICardInPlay;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class AttachedEnergyFactory {
	
	public static AttachedEnergy createNew(
			IGame game, long gameVersion, IUser player, ICardInPlay energyCard, ICardInPlay pokemonCard
	) throws MissingMappingException, MapperException, SQLException {
		
		AttachedEnergy attachedEnergy = new AttachedEnergy(AttachedEnergyTDG.getMaxId(), 1, game, gameVersion, player, energyCard, pokemonCard);
		UoW.getCurrent().registerNew(attachedEnergy);
		
		return attachedEnergy;
		
	}
	
	public static AttachedEnergy createClean(
			long id, long version, IGame game, long gameVersion, IUser player, ICardInPlay energyCard, ICardInPlay pokemonCard
	) {
		
		AttachedEnergy attachedEnergy = new AttachedEnergy(id, version, game, gameVersion, player, energyCard, pokemonCard);
		UoW.getCurrent().registerClean(attachedEnergy);
		
		return attachedEnergy;
		
	}
	
	public static AttachedEnergy createClean(IAttachedEnergy attachedEnergy) {
		return createClean(
				attachedEnergy.getId(), attachedEnergy.getVersion(),
				attachedEnergy.getGame(), attachedEnergy.getGameVersion(), attachedEnergy.getPlayer(),
				attachedEnergy.getEnergyCard(), attachedEnergy.getPokemonCard()
		);
	}

}
