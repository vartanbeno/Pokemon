package dom.model.game;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.deck.IDeck;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.IUser;

public class GameProxy extends DomainObjectProxy<Long, Game> implements IGame {

	protected GameProxy(Long id) {
		super(id);
	}

	@Override
	public IUser getChallenger() {
		return getInnerObject().getChallenger();
	}

	@Override
	public void setChallenger(IUser challenger) {
		getInnerObject().setChallenger(challenger);
	}

	@Override
	public IUser getChallengee() {
		return getInnerObject().getChallengee();
	}

	@Override
	public void setChallengee(IUser challengee) {
		getInnerObject().setChallengee(challengee);
	}

	@Override
	public IDeck getChallengerDeck() {
		return getInnerObject().getChallengerDeck();
	}

	@Override
	public void setChallengerDeck(IDeck challengerDeck) {
		getInnerObject().setChallengerDeck(challengerDeck);
	}

	@Override
	public IDeck getChallengeeDeck() {
		return getInnerObject().getChallengeeDeck();
	}

	@Override
	public void setChallengeeDeck(IDeck challengeeDeck) {
		getInnerObject().setChallengeeDeck(challengeeDeck);
	}
	
	@Override
	public long getCurrentTurn() {
		return getInnerObject().getCurrentTurn();
	}

	@Override
	public void setCurrentTurn(long currentTurn) {
		getInnerObject().setCurrentTurn(currentTurn);
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
	protected Game getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return GameInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
