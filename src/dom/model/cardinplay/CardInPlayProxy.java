package dom.model.cardinplay;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.card.ICard;
import dom.model.cardinplay.mapper.CardInPlayMapper;
import dom.model.deck.IDeck;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class CardInPlayProxy extends DomainObjectProxy<Long, CardInPlay> implements ICardInPlay {

	protected CardInPlayProxy(Long id) {
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
	public IDeck getDeck() {
		return getInnerObject().getDeck();
	}

	@Override
	public void setDeck(IDeck deck) {
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
	public int getStatus() {
		return getInnerObject().getStatus();
	}

	@Override
	public void setStatus(int status) {
		getInnerObject().setStatus(status);
	}

	@Override
	protected CardInPlay getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return CardInPlayMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
