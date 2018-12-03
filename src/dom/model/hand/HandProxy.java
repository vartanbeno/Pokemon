package dom.model.hand;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.hand.mapper.HandInputMapper;
import dom.model.user.IUser;

/**
 * Not using this at all anywhere but keeping it, in case we ever do.
 * 
 * @author vartanbeno
 *
 */
public class HandProxy extends DomainObjectProxy<Long, Hand> implements IHand {

	public HandProxy(Long id) {
		super(id);
	}

	@Override
	public IGame getGame() {
		return getInnerObject().getGame();
	}

	@Override
	public void setGame(IGame game) {
		getInnerObject().setGame(game);
	}

	@Override
	public IUser getPlayer() {
		return getInnerObject().getPlayer();
	}

	@Override
	public void setPlayer(IUser player) {
		getInnerObject().setPlayer(player);
	}

	@Override
	public long getDeck() {
		return getInnerObject().getDeck();
	}

	@Override
	public void setDeck(long deck) {
		getInnerObject().setDeck(deck);
	}

	@Override
	public ICard getCard() {
		return getInnerObject().getCard();
	}

	@Override
	public void setCard(ICard card) {
		getInnerObject().setCard(card);
	}

	@Override
	protected Hand getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return HandInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
