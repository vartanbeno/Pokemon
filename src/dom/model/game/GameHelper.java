package dom.model.game;

import dom.model.deck.DeckHelper;
import dom.model.user.UserHelper;

public class GameHelper {
	
	private long id;
	private UserHelper challenger;
	private UserHelper challengee;
	private DeckHelper challengerDeck;
	private DeckHelper challengeeDeck;
	private int status;
	
	public GameHelper(
			long id,
			UserHelper challenger, UserHelper challengee,
			DeckHelper challengerDeck, DeckHelper challengeeDeck,
			int status
	) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public UserHelper getChallenger() {
		return challenger;
	}

	public void setChallenger(UserHelper challenger) {
		this.challenger = challenger;
	}

	public UserHelper getChallengee() {
		return challengee;
	}

	public void setChallengee(UserHelper challengee) {
		this.challengee = challengee;
	}

	public DeckHelper getChallengerDeck() {
		return challengerDeck;
	}

	public void setChallengerDeck(DeckHelper challengerDeck) {
		this.challengerDeck = challengerDeck;
	}

	public DeckHelper getChallengeeDeck() {
		return challengeeDeck;
	}

	public void setChallengeeDeck(DeckHelper challengeeDeck) {
		this.challengeeDeck = challengeeDeck;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
}
