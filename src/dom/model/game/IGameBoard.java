package dom.model.game;

import java.util.List;

import dom.model.bench.IBench;
import dom.model.discard.IDiscard;
import dom.model.hand.IHand;

public interface IGameBoard {
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public List<IHand> getChallengerHand();
	public void setChallengerHand(List<IHand> challengerHand);
	
	public List<IHand> getChallengeeHand();
	public void setChallengeeHand(List<IHand> challengeeHand);
	
	public List<IBench> getChallengerBench();
	public void setChallengerBench(List<IBench> challengerBench);
	
	public List<IBench> getChallengeeBench();
	public void setChallengeeBench(List<IBench> challengeeBench);
	
	public List<IDiscard> getChallengerDiscarded();
	public void setChallengerDiscarded(List<IDiscard> challengerDiscarded);
	
	public List<IDiscard> getChallengeeDiscarded();
	public void setChallengeeDiscarded(List<IDiscard> challengeeDiscarded);

}
