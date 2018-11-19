package dom.model.cardinplay;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.card.ICard;
import dom.model.deck.IDeck;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class CardInPlayFactory {
	
	public static CardInPlay createNew(long id, long version, IGame game, IUser player, IDeck deck, ICard card, int status)
			throws MissingMappingException, MapperException {
		
		CardInPlay cardInPlay = new CardInPlay(id, version, game, player, deck, card, status);
		UoW.getCurrent().registerNew(cardInPlay);
		
		return cardInPlay;
		
	}
	
	public static CardInPlay createClean(long id, long version, IGame game, IUser player, IDeck deck, ICard card, int status) {
		
		CardInPlay cardInPlay = new CardInPlay(id, version, game, player, deck, card, status);
		UoW.getCurrent().registerClean(cardInPlay);
		
		return cardInPlay;
		
	}
	
	public static CardInPlay registerDirty(long id, long version, IGame game, IUser player, IDeck deck, ICard card, int status)
			throws MissingMappingException, MapperException {
		
		CardInPlay cardInPlay = new CardInPlay(id, version, game, player, deck, card, status);
		UoW.getCurrent().registerDirty(cardInPlay);
		
		return cardInPlay;
		
	}

}
