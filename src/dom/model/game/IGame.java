package dom.model.game;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public interface IGame {
	
	public long getId();
	
	public IUser getChallenger();
	public void setChallenger(IUser challenger);
	
	public IUser getChallengee();
	public void setChallengee(IUser challengee);
	
	public IDeck getChallengerDeck();
	public void setChallengerDeck(IDeck challengerDeck);
	
	public IDeck getChallengeeDeck();
	public void setChallengeeDeck(IDeck challengeeDeck);
	
	public int getStatus();
	public void setStatus(int status);
	
}
