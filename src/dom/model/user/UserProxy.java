package dom.model.user;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.user.mapper.UserMapper;

public class UserProxy extends DomainObjectProxy<Long, User> implements IUser {

	protected UserProxy(Long id) {
		super(id);
	}

	@Override
	public String getUsername() {
		return getInnerObject().getUsername();
	}

	@Override
	public void setUsername(String username) {
		getInnerObject().setUsername(username);
	}

	@Override
	public String getPassword() {
		return getInnerObject().getPassword();
	}

	@Override
	public void setPassword(String password) {
		getInnerObject().setPassword(password);
	}

	@Override
	protected User getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return UserMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
