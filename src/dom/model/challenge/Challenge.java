package dom.model.challenge;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class Challenge implements IChallenge {
	
	private long id;
	private IUser challenger;
	private IUser challengee;
	private int status;
	private IDeck challengerDeck;
	
	public Challenge(long id, IUser challenger, IUser challengee, int status, IDeck challengerDeck) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = status;
		this.challengerDeck = challengerDeck;
	}

	@Override
	public long getId() {
		return id;
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
