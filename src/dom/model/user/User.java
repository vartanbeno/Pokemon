package dom.model.user;

import org.dsrg.soenea.domain.DomainObject;

public class User extends DomainObject<Long> implements IUser {
	
	private String username;
	private String password;
	
	public User(long id, long version, String username, String password) {
		super(id, version);
		this.username = username;
		this.password = password;
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
