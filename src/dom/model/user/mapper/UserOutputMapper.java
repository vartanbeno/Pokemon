package dom.model.user.mapper;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.user.User;
import dom.model.user.tdg.UserTDG;

public class UserOutputMapper extends GenericOutputMapper<Long, User> {
	
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
		int count = UserTDG.update(user.getId(), user.getVersion(), user.getUsername(), user.getPassword());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update user with id: %d.", user.getId()));
	}

	public static void deleteStatic(User user) throws SQLException, LostUpdateException {
		int count = UserTDG.delete(user.getId(), user.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot delete user with id: %d.", user.getId()));
	}

}
