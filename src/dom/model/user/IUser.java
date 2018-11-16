package dom.model.user;

public interface IUser {
	
	public long getId();
	
	public int getVersion();
	public void setVersion(int version);
	
	public String getUsername();
	public void setUsername(String username);
	
	public String getPassword();
	public void setPassword(String password);

}
