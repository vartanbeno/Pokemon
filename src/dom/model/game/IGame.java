package dom.model.game;

import org.dsrg.soenea.domain.interf.IDomainObject;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public interface IGame extends IDomainObject<Long> {
	
	public IUser getChallenger();
	public void setChallenger(IUser challenger);
	
	public IUser getChallengee();
	public void setChallengee(IUser challengee);
	
	public IDeck getChallengerDeck();
	public void setChallengerDeck(IDeck challengerDeck);
	
	public IDeck getChallengeeDeck();
	public void setChallengeeDeck(IDeck challengeeDeck);
	
	public long getCurrentTurn();
	public void setCurrentTurn(long currentTurn);
	
	public int getStatus();
	public void setStatus(int status);
	
}
