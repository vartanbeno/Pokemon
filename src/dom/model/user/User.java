package dom.model.user;

public class User implements IUser {
	
	private long id;
	private int version;
	private String username;
	private String password;
	
	public User(long id, int version, String username, String password) {
		this.id = id;
		this.version = version;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public int getVersion() {
		return version;
	}
	
	@Override
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
}
