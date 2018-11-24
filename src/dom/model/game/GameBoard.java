package dom.model.game;

import java.util.List;

import dom.model.bench.IBench;
import dom.model.discard.IDiscard;
import dom.model.hand.IHand;

public class GameBoard implements IGameBoard {
	
	private IGame game;
	
	private List<IHand> challengerHand;
	private List<IHand> challengeeHand;
	
	private List<IBench> challengerBench;
	private List<IBench> challengeeBench;
	
	private List<IDiscard> challengerDiscarded;
	private List<IDiscard> challengeeDiscarded;
	
	public GameBoard(
			IGame game,
			List<IHand> challengerHand, List<IHand> challengeeHand,
			List<IBench> challengerBench, List<IBench> challengeeBench,
			List<IDiscard> challengerDiscarded, List<IDiscard> challengeeDiscarded
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
	public List<IHand> getChallengerHand() {
		return challengerHand;
	}

	@Override
	public void setChallengerHand(List<IHand> challengerHand) {
		this.challengerHand = challengerHand;
	}

	@Override
	public List<IHand> getChallengeeHand() {
		return challengeeHand;
	}

	@Override
	public void setChallengeeHand(List<IHand> challengeeHand) {
		this.challengeeHand = challengeeHand;
	}

	@Override
	public List<IBench> getChallengerBench() {
		return challengerBench;
	}

	@Override
	public void setChallengerBench(List<IBench> challengerBench) {
		this.challengerBench = challengerBench;
	}

	@Override
	public List<IBench> getChallengeeBench() {
		return challengeeBench;
	}

	@Override
	public void setChallengeeBench(List<IBench> challengeeBench) {
		this.challengeeBench = challengeeBench;
	}

	@Override
	public List<IDiscard> getChallengerDiscarded() {
		return challengerDiscarded;
	}

	@Override
	public void setChallengerDiscarded(List<IDiscard> challengerDiscarded) {
		this.challengerDiscarded = challengerDiscarded;
	}

	@Override
	public List<IDiscard> getChallengeeDiscarded() {
		return challengeeDiscarded;
	}

	@Override
	public void setChallengeeDiscarded(List<IDiscard> challengeeDiscarded) {
		this.challengeeDiscarded = challengeeDiscarded;
	}

}
