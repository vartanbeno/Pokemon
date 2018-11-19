package dom.model.game;

import java.util.List;

import dom.model.cardinplay.ICardInPlay;

public interface IGameBoard {
	
	public IGame getGame();
	public void setGame(IGame game);
	
	public List<ICardInPlay> getChallengerHand();
	public void setChallengerHand(List<ICardInPlay> challengerHand);
	
	public List<ICardInPlay> getChallengeeHand();
	public void setChallengeeHand(List<ICardInPlay> challengeeHand);
	
	public List<ICardInPlay> getChallengerBench();
	public void setChallengerBench(List<ICardInPlay> challengerBench);
	
	public List<ICardInPlay> getChallengeeBench();
	public void setChallengeeBench(List<ICardInPlay> challengeeBench);
	
	public List<ICardInPlay> getChallengerDiscarded();
	public void setChallengerDiscarded(List<ICardInPlay> challengerDiscarded);
	
	public List<ICardInPlay> getChallengeeDiscarded();
	public void setChallengeeDiscarded(List<ICardInPlay> challengeeDiscarded);

}
