package dom.model.challenge;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.challenge.mapper.ChallengeInputMapper;
import dom.model.deck.IDeck;
import dom.model.user.IUser;

/**
 * Not using this at all anywhere but keeping it, in case we ever do.
 * 
 * @author vartanbeno
 *
 */
public class ChallengeProxy extends DomainObjectProxy<Long, Challenge> implements IChallenge {

	public ChallengeProxy(Long id) {
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
	public int getStatus() {
		return getInnerObject().getStatus();
	}

	@Override
	public void setStatus(int status) {
		getInnerObject().setStatus(status);
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
	protected Challenge getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return ChallengeInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
