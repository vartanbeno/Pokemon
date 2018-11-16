package dom.model.deck;

import dom.model.user.IUser;

public interface IDeck {
	
	public long getId();
	
	public IUser getPlayer();
	public void setPlayer(IUser player);

}
