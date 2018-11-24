package dom.model.bench;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.bench.tdg.BenchTDG;
import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class BenchFactory {
	
	public static Bench createNew(IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException, SQLException {
		
		Bench benchCard = new Bench(BenchTDG.getMaxId(), 1, game, player, deck, card);
		UoW.getCurrent().registerNew(benchCard);
		
		return benchCard;
		
	}
	
	public static Bench createClean(long id, long version, IGame game, IUser player, long deck, ICard card) {
		
		Bench benchCard = new Bench(id, version, game, player, deck, card);
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
				benchCard.getCard()
		);
	}
	
	public static Bench registerDirty(long id, long version, IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException {
		
		Bench benchCard = new Bench(id, version, game, player, deck, card);
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
				benchCard.getCard()
		);
	}

}
