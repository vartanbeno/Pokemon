package dom.model.game;

import org.dsrg.soenea.domain.DomainObject;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class Game extends DomainObject<Long> implements IGame {
	
	private IUser challenger;
	private IUser challengee;
	private IDeck challengerDeck;
	private IDeck challengeeDeck;
	private long currentTurn;
	private long turn;
	private int status;
	
	public Game(
			long id, long version,
			IUser challenger, IUser challengee,
			IDeck challengerDeck, IDeck challengeeDeck,
			long currentTurn, long turn, int status
	) {
		super(id, version);
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
		this.currentTurn = currentTurn;
		this.turn = turn;
		this.status = status;
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
	public long getCurrentTurn() {
		return currentTurn;
	}
	
	@Override
	public void setCurrentTurn(long currentTurn) {
		this.currentTurn = currentTurn;
	}
	
	@Override
	public long getTurn() {
		return turn;
	}
	
	@Override
	public void setTurn(long turn) {
		this.turn = turn;
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
