package dom.model.game;

import java.util.List;

import dom.model.cardinplay.CardInPlayHelper;
import dom.model.deck.DeckWithCardsHelper;
import dom.model.user.UserHelper;

public class GameBoardHelper {
	
	private long id;
	private UserHelper challenger;
	private UserHelper challengee;
	private DeckWithCardsHelper challengerDeck;
	private DeckWithCardsHelper challengeeDeck;
	private List<CardInPlayHelper> challengerHandCards;
	private List<CardInPlayHelper> challengeeHandCards;
	private List<CardInPlayHelper> challengerBenchedCards;
	private List<CardInPlayHelper> challengeeBenchedCards;
	private List<CardInPlayHelper> challengerDiscardedCards;
	private List<CardInPlayHelper> challengeeDiscardedCards;
	private int status;
	
	public GameBoardHelper(
			long id,
			UserHelper challenger, UserHelper challengee,
			DeckWithCardsHelper challengerDeck, DeckWithCardsHelper challengeeDeck,
			List<CardInPlayHelper> challengerHandCards, List<CardInPlayHelper> challengeeHandCards,
			List<CardInPlayHelper> challengerBenchedCards, List<CardInPlayHelper> challengeeBenchedCards,
			List<CardInPlayHelper> challengerDiscardedCards, List<CardInPlayHelper> challengeeDiscardedCards,
			int status
	) {
		super();
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
		this.challengerHandCards = challengerHandCards;
		this.challengeeHandCards = challengeeHandCards;
		this.challengerBenchedCards = challengerBenchedCards;
		this.challengeeBenchedCards = challengeeBenchedCards;
		this.challengerDiscardedCards = challengerDiscardedCards;
		this.challengeeDiscardedCards = challengeeDiscardedCards;
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

	public List<CardInPlayHelper> getChallengerHandCards() {
		return challengerHandCards;
	}

	public void setChallengerHandCards(List<CardInPlayHelper> challengerHandCards) {
		this.challengerHandCards = challengerHandCards;
	}

	public List<CardInPlayHelper> getChallengeeHandCards() {
		return challengeeHandCards;
	}

	public void setChallengeeHandCards(List<CardInPlayHelper> challengeeHandCards) {
		this.challengeeHandCards = challengeeHandCards;
	}

	public List<CardInPlayHelper> getChallengerBenchedCards() {
		return challengerBenchedCards;
	}

	public void setChallengerBenchedCards(List<CardInPlayHelper> challengerBenchedCards) {
		this.challengerBenchedCards = challengerBenchedCards;
	}

	public List<CardInPlayHelper> getChallengeeBenchedCards() {
		return challengeeBenchedCards;
	}

	public void setChallengeeBenchedCards(List<CardInPlayHelper> challengeeBenchedCards) {
		this.challengeeBenchedCards = challengeeBenchedCards;
	}

	public List<CardInPlayHelper> getChallengerDiscardedCards() {
		return challengerDiscardedCards;
	}

	public void setChallengerDiscardedCards(List<CardInPlayHelper> challengerDiscardedCards) {
		this.challengerDiscardedCards = challengerDiscardedCards;
	}

	public List<CardInPlayHelper> getChallengeeDiscardedCards() {
		return challengeeDiscardedCards;
	}

	public void setChallengeeDiscardedCards(List<CardInPlayHelper> challengeeDiscardedCards) {
		this.challengeeDiscardedCards = challengeeDiscardedCards;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
