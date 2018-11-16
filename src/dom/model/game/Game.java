package dom.model.game;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class Game implements IGame {
	
	private long id;
	private IUser challenger;
	private IUser challengee;
	private IDeck challengerDeck;
	private IDeck challengeeDeck;
	private int status;
	
	public Game(long id, IUser challenger, IUser challengee, IDeck challengerDeck, IDeck challengeeDeck, int status) {
		super();
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
		this.status = status;
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
	public IDeck getChallengerDeck() {
		return challengerDeck;
	}

	@Override
	public void setChallengerDeck(IDeck challengerDeck) {
		this.challengerDeck = challengerDeck;
	}

	@Override
	public IDeck getChallengeeDeck() {
		return challengeeDeck;
	}

	@Override
	public void setChallengeeDeck(IDeck challengeeDeck) {
		this.challengeeDeck = challengeeDeck;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

}
