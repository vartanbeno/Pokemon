package dom.model.user;

public class UserHelper {
	
	private long id;
	private int version;
	private String username;
	private String password;
	
	public UserHelper(long id, int version, String username, String password) {
		this.id = id;
		this.version = version;
		this.username = username;
		this.password = password;
	}
	
	public long getId() {
		return id;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}
