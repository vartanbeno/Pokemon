package dom.model.discard;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.card.ICard;
import dom.model.discard.tdg.DiscardTDG;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class DiscardFactory {
	
	public static Discard createNew(IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException, SQLException {
		
		Discard discardCard = new Discard(DiscardTDG.getMaxId(), 1, game, player, deck, card);
		UoW.getCurrent().registerNew(discardCard);
		
		return discardCard;
		
	}
	
	public static Discard createClean(long id, long version, IGame game, IUser player, long deck, ICard card) {
		
		Discard discardCard = new Discard(id, version, game, player, deck, card);
		UoW.getCurrent().registerClean(discardCard);
		
		return discardCard;
		
	}
	
	public static Discard createClean(IDiscard discardCard) {
		return createClean(
				discardCard.getId(),
				discardCard.getVersion(),
				discardCard.getGame(),
				discardCard.getPlayer(),
				discardCard.getDeck(),
				discardCard.getCard()
		);
	}
	
	public static Discard registerDirty(long id, long version, IGame game, IUser player, long deck, ICard card)
			throws MissingMappingException, MapperException {
		
		Discard discardCard = new Discard(id, version, game, player, deck, card);
		UoW.getCurrent().registerDirty(discardCard);
		
		return discardCard;
		
	}
	
	public static Discard registerDirty(IDiscard discardCard)
			throws MissingMappingException, MapperException {
		return registerDirty(
				discardCard.getId(),
				discardCard.getVersion(),
				discardCard.getGame(),
				discardCard.getPlayer(),
				discardCard.getDeck(),
				discardCard.getCard()
		);
	}

}
