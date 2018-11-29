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
			UserTDG.insert(user.getId(), user.getVersion(), user.getUsername(), user.getPassword());
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(User user) throws MapperException {
		try {
			int count = UserTDG.update(user.getId(), user.getVersion(), user.getUsername(), user.getPassword());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot update user with id: %d.", user.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(User user) throws MapperException {
		try {
			int count = UserTDG.delete(user.getId(), user.getVersion());
			if (count == 0) throw new LostUpdateException(String.format("Lost update: cannot delete user with id: %d.", user.getId()));
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

}
