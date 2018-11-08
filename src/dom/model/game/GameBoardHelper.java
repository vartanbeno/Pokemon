package dom.model.game;

import java.util.List;

import dom.model.deck.DeckWithCardsHelper;
import dom.model.handcard.HandCardHelper;
import dom.model.user.UserHelper;

public class GameBoardHelper {
	
	private long id;
	private UserHelper challenger;
	private UserHelper challengee;
	private DeckWithCardsHelper challengerDeck;
	private DeckWithCardsHelper challengeeDeck;
	private List<HandCardHelper> challengerHand;
	private List<HandCardHelper> challengeeHand;
	
	public GameBoardHelper(
			long id,
			UserHelper challenger, UserHelper challengee,
			DeckWithCardsHelper challengerDeck, DeckWithCardsHelper challengeeDeck,
			List<HandCardHelper> challengerHand, List<HandCardHelper> challengeeHand
	) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
		this.challengerHand = challengerHand;
		this.challengeeHand = challengeeHand;
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

	public DeckWithCardsHelper getChallengerDeck() {
		return challengerDeck;
	}

	public void setChallengerDeck(DeckWithCardsHelper challengerDeck) {
		this.challengerDeck = challengerDeck;
	}

	public DeckWithCardsHelper getChallengeeDeck() {
		return challengeeDeck;
	}

	public void setChallengeeDeck(DeckWithCardsHelper challengeeDeck) {
		this.challengeeDeck = challengeeDeck;
	}

	public List<HandCardHelper> getChallengerHand() {
		return challengerHand;
	}

	public void setChallengerHand(List<HandCardHelper> challengerHand) {
		this.challengerHand = challengerHand;
	}

	public List<HandCardHelper> getChallengeeHand() {
		return challengeeHand;
	}

	public void setChallengeeHand(List<HandCardHelper> challengeeHand) {
		this.challengeeHand = challengeeHand;
	}
	
}
