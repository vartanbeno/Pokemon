package dom.model.hand;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.hand.tdg.HandTDG;
import dom.model.user.IUser;

public class HandFactory {
	
	public static Hand createNew(IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException, SQLException {
		
		Hand handCard = new Hand(HandTDG.getMaxId(), 1, game, player, deck, card);
		UoW.getCurrent().registerNew(handCard);
		
		return handCard;
		
	}
	
	public static Hand createClean(long id, long version, IGame game, IUser player, long deck, ICard card) {
		
		Hand handCard = new Hand(id, version, game, player, deck, card);
		UoW.getCurrent().registerClean(handCard);
		
		return handCard;
		
	}
	
	public static Hand registerDirty(long id, long version, IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException {
		
		Hand handCard = new Hand(id, version, game, player, deck, card);
		UoW.getCurrent().registerDirty(handCard);
		
		return handCard;
		
	}
	
	public static Hand registerDeleted(long id, long version, IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException {
		
		Hand handCard = new Hand(id, version, game, player, deck, card);
		UoW.getCurrent().registerRemoved(handCard);
		
		return handCard;
		
	}

}
