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
	
	public static Hand createClean(IHand handCard) {
		return createClean(
				handCard.getId(),
				handCard.getVersion(),
				handCard.getGame(),
				handCard.getPlayer(),
				handCard.getDeck(),
				handCard.getCard()
		);
	}
	
	public static Hand registerDirty(long id, long version, IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException {
		
		Hand handCard = new Hand(id, version, game, player, deck, card);
		UoW.getCurrent().registerDirty(handCard);
		
		return handCard;
		
	}
	
	public static Hand registerDirty(IHand handCard)
			throws MissingMappingException, MapperException {
		return registerDirty(
				handCard.getId(),
				handCard.getVersion(),
				handCard.getGame(),
				handCard.getPlayer(),
				handCard.getDeck(),
				handCard.getCard()
		);
	}
	
	public static Hand registerDeleted(long id, long version, IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException {
		
		Hand handCard = new Hand(id, version, game, player, deck, card);
		UoW.getCurrent().registerRemoved(handCard);
		
		return handCard;
		
	}
	
	public static Hand registerDeleted(IHand handCard)
			throws MissingMappingException, MapperException {
		return registerDeleted(
				handCard.getId(),
				handCard.getVersion(),
				handCard.getGame(),
				handCard.getPlayer(),
				handCard.getDeck(),
				handCard.getCard()
		);
	}

}
