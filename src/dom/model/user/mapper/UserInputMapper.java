package dom.model.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.user.IUser;
import dom.model.user.User;
import dom.model.user.UserFactory;
import dom.model.user.tdg.UserFinder;

public class UserInputMapper {
	
	public static List<IUser> findAll() throws SQLException {
		
		ResultSet rs = UserFinder.findAll();
		
		List<IUser> users = buildUsers(rs);
		rs.close();
		
		return users;
		
	}
	
	public static User findById(long id) throws SQLException {
		
		User user = getFromIdentityMap(id);
		if (user != null) return user;
		
		ResultSet rs = UserFinder.findById(id);
		
		user = rs.next() ? buildUser(rs) : null;
		rs.close();

		return user;
		
	}
	
	public static User findByUsername(String username) throws SQLException {
		
		User user = null;
		
		ResultSet rs = UserFinder.findByUsername(username);
		
		if (rs.next()) {
			
			long id = rs.getLong("id");
			user = getFromIdentityMap(id);
			if (user != null) return user;
			
			user = buildUser(rs);
			rs.close();
			
		}
		
		return user;
		
	}
	
	public static User findByUsernameAndPassword(String username, String password) throws SQLException {
		
		User user = null;
		
		ResultSet rs = UserFinder.findByUsernameAndPassword(username, password);
		
		if (rs.next()) {
			
			long id = rs.getLong("id");
			user = getFromIdentityMap(id);
			if (user != null) return user;
			
			user = buildUser(rs);
			rs.close();
			
		}
		
		return user;
		
	}
	
	public static User buildUser(ResultSet rs) throws SQLException {
		
		return UserFactory.createClean(
				rs.getLong("id"),
				rs.getInt("version"),
				rs.getString("username"),
				rs.getString("password")
		);
		
	}
	
	public static List<IUser> buildUsers(ResultSet rs) throws SQLException {
		
		List<IUser> users = new ArrayList<IUser>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			User user = getFromIdentityMap(id);
			
			if (user == null) user = buildUser(rs);
			
			users.add(user);
			
		}
		
		rs.close();
		
		return users;
		
	}
	
	public static User getFromIdentityMap(long id) {
		
		User user = null;
		
		try {
			user = IdentityMap.get(id, User.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) {
			// nobody cares
		}
		
		return user;
		
	}

}
