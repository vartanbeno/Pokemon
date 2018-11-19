package dom.model.game;

import java.util.List;

import dom.model.cardinplay.ICardInPlay;

public class GameBoard implements IGameBoard {
	
	private IGame game;
	
	private List<ICardInPlay> challengerHand;
	private List<ICardInPlay> challengeeHand;
	
	private List<ICardInPlay> challengerBench;
	private List<ICardInPlay> challengeeBench;
	
	private List<ICardInPlay> challengerDiscarded;
	private List<ICardInPlay> challengeeDiscarded;
	
	public GameBoard(
			IGame game,
			List<ICardInPlay> challengerHand, List<ICardInPlay> challengeeHand,
			List<ICardInPlay> challengerBench, List<ICardInPlay> challengeeBench,
			List<ICardInPlay> challengerDiscarded, List<ICardInPlay> challengeeDiscarded
	) {
		this.game = game;
		this.challengerHand = challengerHand;
		this.challengeeHand = challengeeHand;
		this.challengerBench = challengerBench;
		this.challengeeBench = challengeeBench;
		this.challengerDiscarded = challengerDiscarded;
		this.challengeeDiscarded = challengeeDiscarded;
	}

	@Override
	public IGame getGame() {
		return game;
	}
	
	@Override
	public void setGame(IGame game) {
		this.game = game;
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

}
