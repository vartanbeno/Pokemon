package dom.model.user.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.user.IUser;
import dom.model.user.User;
import dom.model.user.tdg.UserTDG;

public class UserMapper extends GenericOutputMapper<Long, User> {
	
	@Override
	public void insert(User user) throws MapperException {
		try {
			insertStatic(user);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(User user) throws MapperException {
		try {
			updateStatic(user);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(User user) throws MapperException {
		try {
			deleteStatic(user);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(User user) throws SQLException {
		UserTDG.insert(user.getId(), user.getVersion(), user.getUsername(), user.getPassword());
	}

	public static void updateStatic(User user) throws SQLException, LostUpdateException {
		int count = UserTDG.update(user.getUsername(), user.getPassword(), user.getId(), user.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update user with id: %d.", user.getId()));
	}

	public static void deleteStatic(User user) throws SQLException, LostUpdateException {
		int count = UserTDG.delete(user.getId(), user.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot delete user with id: %d.", user.getId()));
	}
	
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
	
	public static User buildUser(ResultSet rs) throws SQLException {
		
		return new User(
				rs.getLong("id"),
				rs.getInt("version"),
				rs.getString("username"),
				rs.getString("password")
			);
		
	}
	
	public static List<IUser> buildUsers(ResultSet rs) throws SQLException {
		
		List<IUser> users = new ArrayList<IUser>();
		
		while (rs.next()) {
			
			User user = buildUser(rs);
			users.add(user);
			
		}
		
		rs.close();
		
		return users;
		
	}

}
