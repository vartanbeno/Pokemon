package dom.model.deck;

import dom.model.user.UserHelper;

public class DeckHelper {
	
	private long id;
	private UserHelper player;
	
	public DeckHelper(long id, UserHelper player) {
		this.id = id;
		this.player = player;
	}

	public long getId() {
		return id;
	}

	public UserHelper getPlayer() {
		return player;
	}

	public void setPlayer(UserHelper player) {
		this.player = player;
	}
	
}
