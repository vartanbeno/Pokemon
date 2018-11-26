package dom.model.bench;

import java.util.List;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.bench.mapper.BenchInputMapper;
import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class BenchProxy extends DomainObjectProxy<Long, Bench> implements IBench {

	protected BenchProxy(Long id) {
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
	public ICard getPredecessor() {
		return getInnerObject().getPredecessor();
	}
	
	@Override
	public void setPredecessor(ICard predecessor) {
		getInnerObject().setPredecessor(predecessor);
	}
	
	@Override
	public List<IAttachedEnergy> getAttachedEnergyCards() {
		return getInnerObject().getAttachedEnergyCards();
	}

	@Override
	public void setAttachedEnergyCards(List<IAttachedEnergy> attachedEnergyCards) {
		getInnerObject().setAttachedEnergyCards(attachedEnergyCards);
	}

	@Override
	protected Bench getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return BenchInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
