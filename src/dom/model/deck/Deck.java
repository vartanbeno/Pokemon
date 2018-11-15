package dom.model.deck;

import dom.model.user.IUser;

public class Deck implements IDeck {
	
	private long id;
	private IUser player;
	
	public Deck(long id, IUser player) {
		this.id = id;
		this.player = player;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public IUser getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(IUser player) {
		this.player = player;
	}

}
