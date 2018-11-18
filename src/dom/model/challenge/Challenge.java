package dom.model.challenge;

import org.dsrg.soenea.domain.DomainObject;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class Challenge extends DomainObject<Long> implements IChallenge {
	
	private IUser challenger;
	private IUser challengee;
	private int status;
	private IDeck challengerDeck;
	
	public Challenge(long id, long version, IUser challenger, IUser challengee, int status, IDeck challengerDeck) {
		super(id, version);
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = status;
		this.challengerDeck = challengerDeck;
	}

	@Override
	public IUser getChallenger() {
		return challenger;
	}

	@Override
	public void setChallenger(IUser challenger) {
		this.challenger = challenger;
	}

	@Override
	public IUser getChallengee() {
		return challengee;
	}

	@Override
	public void setChallengee(IUser challengee) {
		this.challengee = challengee;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public IDeck getChallengerDeck() {
		return challengerDeck;
	}
	
	@Override
	public void setChallengerDeck(IDeck challengerDeck) {
		this.challengerDeck = challengerDeck;
	}

}
