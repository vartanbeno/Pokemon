package dom.model.user.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class UserFinder {
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", UserTDG.getColumns(), UserTDG.getTableName());

	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s WHERE id = ?;", UserTDG.getColumns(), UserTDG.getTableName());

	private static final String FIND_BY_USERNAME = String.format("SELECT %1$s FROM %2$s WHERE username = ?;", UserTDG.getColumns(), UserTDG.getTableName());
	
	private static final String FIND_BY_USERNAME_AND_PASSWORD = String.format("SELECT %1$s "
			+ "FROM %2$s WHERE username = ? AND password = SHA2(?, 256);", UserTDG.getColumns(), UserTDG.getTableName());
	
	public static ResultSet findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByUsername(String username) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_USERNAME);
		ps.setString(1, username);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByUsernameAndPassword(String username, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_USERNAME_AND_PASSWORD);
		ps.setString(1, username);
		ps.setString(2, password);
		
		return ps.executeQuery();
	}

}
