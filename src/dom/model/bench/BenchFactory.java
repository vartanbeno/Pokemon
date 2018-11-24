package dom.model.bench;

import java.sql.SQLException;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.bench.tdg.BenchTDG;
import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class BenchFactory {
	
	public static Bench createNew(IGame game, IUser player, long deck, ICard card, List<IAttachedEnergy> attachedEnergyCards)
			throws MissingMappingException, MapperException, SQLException {
		
		Bench benchCard = new Bench(BenchTDG.getMaxId(), 1, game, player, deck, card, attachedEnergyCards);
		UoW.getCurrent().registerNew(benchCard);
		
		return benchCard;
		
	}
	
	public static Bench createClean(long id, long version, IGame game, IUser player, long deck, ICard card, List<IAttachedEnergy> attachedEnergyCards) {
		
		Bench benchCard = new Bench(id, version, game, player, deck, card, attachedEnergyCards);
		UoW.getCurrent().registerClean(benchCard);
		
		return benchCard;
		
	}
	
	public static Bench createClean(IBench benchCard) {
		return createClean(
				benchCard.getId(),
				benchCard.getVersion(),
				benchCard.getGame(),
				benchCard.getPlayer(),
				benchCard.getDeck(),
				benchCard.getCard(),
				benchCard.getAttachedEnergyCards()
		);
	}
	
	public static Bench registerDirty(long id, long version, IGame game, IUser player, long deck, ICard card, List<IAttachedEnergy> attachedEnergyCards)
			throws MissingMappingException, MapperException {
		
		Bench benchCard = new Bench(id, version, game, player, deck, card, attachedEnergyCards);
		UoW.getCurrent().registerDirty(benchCard);
		
		return benchCard;
		
	}
	
	public static Bench registerDirty(IBench benchCard)
			throws MissingMappingException, MapperException {
		return registerDirty(
				benchCard.getId(),
				benchCard.getVersion(),
				benchCard.getGame(),
				benchCard.getPlayer(),
				benchCard.getDeck(),
				benchCard.getCard(),
				benchCard.getAttachedEnergyCards()
		);
	}
	
	public static Bench registerDeleted(long id, long version, IGame game, IUser player, long deck, ICard card, List<IAttachedEnergy> attachedEnergyCards)
			throws MissingMappingException, MapperException {
		
		Bench benchCard = new Bench(id, version, game, player, deck, card, attachedEnergyCards);
		UoW.getCurrent().registerRemoved(benchCard);
		
		return benchCard;
		
	}
	
	public static Bench registerDeleted(IBench benchCard)
			throws MissingMappingException, MapperException {
		return registerDeleted(benchCard);
	}

}
