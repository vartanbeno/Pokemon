package dom.model.discard;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.card.ICard;
import dom.model.discard.mapper.DiscardInputMapper;
import dom.model.game.IGame;
import dom.model.user.IUser;

/**
 * Not using this at all anywhere but keeping it, in case we ever do.
 * 
 * @author vartanbeno
 *
 */
public class DiscardProxy extends DomainObjectProxy<Long, Discard> implements IDiscard {

	public DiscardProxy(Long id) {
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
	protected Discard getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return DiscardInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
