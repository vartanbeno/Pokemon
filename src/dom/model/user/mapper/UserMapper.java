package dom.model.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.user.IUser;
import dom.model.user.User;
import dom.model.user.tdg.UserTDG;

public class UserMapper {
	
	public static List<IUser> findAll() throws SQLException {
		
		ResultSet rs = UserTDG.findAll();
		
		List<IUser> users = buildUsers(rs);
		rs.close();
		
		return users;
		
	}
	
	public static User findById(long id) throws SQLException {
		
		ResultSet rs = UserTDG.findById(id);
		
		User user = rs.next() ? buildUser(rs) : null;
		rs.close();

		return user;
		
	}
	
	public static User findByUsername(String username) throws SQLException {
		
		ResultSet rs = UserTDG.findByUsername(username);
		
		User user = rs.next() ? buildUser(rs) : null;
		rs.close();

		return user;
		
	}
	
	public static User findByUsernameAndPassword(String username, String password) throws SQLException {
		
		ResultSet rs = UserTDG.findByUsernameAndPassword(username, password);
		
		User user = rs.next() ? buildUser(rs) : null;
		rs.close();

		return user;
		
	}
	
	public static void insert(User user) throws SQLException {
		UserTDG.insert(user.getId(), user.getVersion(), user.getUsername(), user.getPassword());
	}
	
	public static void update(User user) throws SQLException {
		UserTDG.update(user.getUsername(), user.getPassword(), user.getId(), user.getVersion());
	}
	
	public static void delete(User user) throws SQLException {
		UserTDG.delete(user.getId(), user.getVersion());
	}
	
	private static User buildUser(ResultSet rs) throws SQLException {
		
		return new User(
				rs.getLong("id"),
				rs.getInt("version"),
				rs.getString("username"),
				rs.getString("password")
			);
		
	}
	
	private static List<IUser> buildUsers(ResultSet rs) throws SQLException {
		
		ArrayList<IUser> users = new ArrayList<IUser>();
		
		while (rs.next()) {
			
			User user = buildUser(rs);
			users.add(user);
			
		}
		
		return users;
		
	}

}
