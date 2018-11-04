package dom.model.challenge;

import dom.model.user.UserHelper;

public class ChallengeHelper {
	
	private long id;
	private UserHelper challenger;
	private UserHelper challengee;
	private int status;
	
	public ChallengeHelper(long id, UserHelper challenger, UserHelper challengee, int status) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = status;
	}
	
	public ChallengeHelper(long id, UserHelper challenger, UserHelper challengee) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = ChallengeStatus.open.ordinal();
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
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
}
