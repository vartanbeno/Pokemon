package dom.model.game;

import java.util.List;

import dom.model.cardinplay.ICardInPlay;
import dom.model.deck.IDeck;
import dom.model.user.IUser;

public interface IGameBoard {
	
	public long getId();
	
	public IUser getChallenger();
	public void setChallenger(IUser challenger);
	
	public IUser getChallengee();
	public void setChallengee(IUser challengee);
	
	public IDeck getChallengerDeck();
	public void setChallengerDeck(IDeck challengerDeck);
	
	public IDeck getChallengeeDeck();
	public void setChallengeeDeck(IDeck challengeeDeck);
	
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
	
	public int getStatus();
	public void setStatus(int status);

}
