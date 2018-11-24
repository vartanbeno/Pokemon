package dom.model.bench;

import java.util.List;

import org.dsrg.soenea.domain.DomainObject;

import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.card.ICard;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class Bench extends DomainObject<Long> implements IBench {
	
	private IGame game;
	private IUser player;
	private long deck;
	private ICard card;
	private List<IAttachedEnergy> attachedEnergyCards;

	protected Bench(long id, long version, IGame game, IUser player, long deck, ICard card, List<IAttachedEnergy> attachedEnergyCards) {
		super(id, version);
		this.game = game;
		this.player = player;
		this.deck = deck;
		this.card = card;
		this.attachedEnergyCards = attachedEnergyCards;
	}

	@Override
	public IGame getGame() {
		return game;
	}

	@Override
	public void setGame(IGame game) {
		this.game = game;
	}

	@Override
	public IUser getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(IUser player) {
		this.player = player;
	}

	@Override
	public long getDeck() {
		return deck;
	}

	@Override
	public void setDeck(long deck) {
		this.deck = deck;
	}

	@Override
	public ICard getCard() {
		return card;
	}

	@Override
	public void setCard(ICard card) {
		this.card = card;
	}
	
	@Override
	public List<IAttachedEnergy> getAttachedEnergyCards() {
		return attachedEnergyCards;
	}

	@Override
	public void setAttachedEnergyCards(List<IAttachedEnergy> attachedEnergyCards) {
		this.attachedEnergyCards = attachedEnergyCards;
	}
	
}
