package dom.model.challenge;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public interface IChallenge {
	
	public long getId();
	
	public IUser getChallenger();
	public void setChallenger(IUser challenger);
	
	public IUser getChallengee();
	public void setChallengee(IUser challengee);
	
	public int getStatus();
	public void setStatus(int status);
	
	public IDeck getChallengerDeck();
	public void setChallengerDeck(IDeck challengerDeck);

}
