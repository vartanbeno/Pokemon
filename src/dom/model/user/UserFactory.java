package dom.model.user;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.user.tdg.UserTDG;

public class UserFactory {
	
	public static User createNew(String username, String password)
			throws SQLException, MissingMappingException, MapperException {
		
		User user = new User(UserTDG.getMaxId(), 1, username, password);
		UoW.getCurrent().registerNew(user);
		
		return user;
		
	}
	
	public static User createClean(long id, long version, String username, String password) {
		
		User user = new User(id, version, username, password);
		UoW.getCurrent().registerClean(user);
		
		return user;
		
	}

}
