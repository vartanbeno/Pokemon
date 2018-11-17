package dom.model.game;

import java.util.List;

import dom.model.cardinplay.ICardInPlay;
import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class GameBoard implements IGameBoard {
	
	private long id;
	
	private IUser challenger;
	private IUser challengee;
	
	private IDeck challengerDeck;
	private IDeck challengeeDeck;
	
	private List<ICardInPlay> challengerHand;
	private List<ICardInPlay> challengeeHand;
	
	private List<ICardInPlay> challengerBench;
	private List<ICardInPlay> challengeeBench;
	
	private List<ICardInPlay> challengerDiscarded;
	private List<ICardInPlay> challengeeDiscarded;
	
	private int status;
	
	public GameBoard(
			long id,
			IUser challenger, IUser challengee,
			IDeck challengerDeck, IDeck challengeeDeck,
			List<ICardInPlay> challengerHand, List<ICardInPlay> challengeeHand,
			List<ICardInPlay> challengerBench, List<ICardInPlay> challengeeBench,
			List<ICardInPlay> challengerDiscarded, List<ICardInPlay> challengeeDiscarded,
			int status
	) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
		this.challengerHand = challengerHand;
		this.challengeeHand = challengeeHand;
		this.challengerBench = challengerBench;
		this.challengeeBench = challengeeBench;
		this.challengerDiscarded = challengerDiscarded;
		this.challengeeDiscarded = challengeeDiscarded;
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
	public List<ICardInPlay> getChallengerHand() {
		return challengerHand;
	}

	@Override
	public void setChallengerHand(List<ICardInPlay> challengerHand) {
		this.challengerHand = challengerHand;
	}

	@Override
	public List<ICardInPlay> getChallengeeHand() {
		return challengeeHand;
	}

	@Override
	public void setChallengeeHand(List<ICardInPlay> challengeeHand) {
		this.challengeeHand = challengeeHand;
	}

	@Override
	public List<ICardInPlay> getChallengerBench() {
		return challengerBench;
	}

	@Override
	public void setChallengerBench(List<ICardInPlay> challengerBench) {
		this.challengerBench = challengerBench;
	}

	@Override
	public List<ICardInPlay> getChallengeeBench() {
		return challengeeBench;
	}

	@Override
	public void setChallengeeBench(List<ICardInPlay> challengeeBench) {
		this.challengeeBench = challengeeBench;
	}

	@Override
	public List<ICardInPlay> getChallengerDiscarded() {
		return challengerDiscarded;
	}

	@Override
	public void setChallengerDiscarded(List<ICardInPlay> challengerDiscarded) {
		this.challengerDiscarded = challengerDiscarded;
	}

	@Override
	public List<ICardInPlay> getChallengeeDiscarded() {
		return challengeeDiscarded;
	}

	@Override
	public void setChallengeeDiscarded(List<ICardInPlay> challengeeDiscarded) {
		this.challengeeDiscarded = challengeeDiscarded;
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
