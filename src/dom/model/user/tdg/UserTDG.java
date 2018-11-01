package dom.model.user.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class UserTDG {
	
	private static final String FIND_ALL = "SELECT id, version, username, password FROM users;";

	private static final String FIND_BY_ID = "SELECT id, version, username, password FROM users WHERE id = ?;";

	private static final String FIND_BY_USERNAME = "SELECT id, version, username, password "
			+ "FROM users WHERE username = ?;";
		
	private static final String INSERT = "INSERT INTO users (id, version, username, password) "
			+ "VALUES (?, ?, ?, ?);";
	
	private static final String UPDATE = "UPDATE users SET username = ?, password = ?, version = (version + 1) "
			+ "WHERE id = ? AND version = ?";
	
	private static final String DELETE = "DELETE FROM users WHERE id = ? AND version = ?;";
	
	public static ResultSet findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		ps.close();
		
		return rs;
	}
	
	public static ResultSet findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		ps.close();
		
		return rs;
	}
	
	public static ResultSet findByUsername(String username) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_USERNAME);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		ps.close();
		
		return rs;
	}
	
	public static int insert(long id, int version, String username, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setInt(2, version);
		ps.setString(3, username);
		ps.setString(4, password);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int update(long id, int version, String username, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setString(1, username);
		ps.setString(2, password);
		ps.setLong(3, id);
		ps.setInt(4, version);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int delete(long id, int version) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1, id);
		ps.setInt(2, version);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
}
