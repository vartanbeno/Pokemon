package dom.model.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.user.IUser;
import dom.model.user.User;
import dom.model.user.tdg.UserTDG;;

public class UserMapper {
	
	public static List<User> findAll() throws SQLException {
		ResultSet rs = UserTDG.findAll();
		List<User> users = new ArrayList<User>();
		User user = null;
		
		while (rs.next()) {
			user = new User(
					rs.getLong("id"),
					rs.getInt("version"),
					rs.getString("username"),
					rs.getString("password")
			);
			users.add(user);
		}
		
		rs.close();
		return users;
	}
	
	public static User findById(long id) throws SQLException {
		ResultSet rs = UserTDG.findById(id);
		User user = null;
		
		if (rs.next()) {
			user = new User(
					rs.getLong("id"),
					rs.getInt("version"),
					rs.getString("username"),
					rs.getString("password")
			);
		}
		
		rs.close();
		return user;
	}
	
	public static User findByUsername(String username) throws SQLException {
		ResultSet rs = UserTDG.findByUsername(username);
		User user = null;
		
		while (rs.next()) {
			user = new User(
					rs.getLong("id"),
					rs.getInt("version"),
					rs.getString("username"),
					rs.getString("password")
			);
		}
		
		rs.close();
		return user;
	}
	
	public static void insert(IUser user) throws SQLException {
		UserTDG.insert(
				user.getId(),
				user.getVersion(),
				user.getUsername(),
				user.getPassword()
		);
	}
	
	public static void update(IUser user) throws SQLException {
		UserTDG.update(
				user.getId(),
				user.getVersion(),
				user.getUsername(),
				user.getPassword()
		);
	}
	
	public static void delete(IUser user) throws SQLException {
		UserTDG.delete(
				user.getId(),
				user.getVersion()
		);
	}

}
