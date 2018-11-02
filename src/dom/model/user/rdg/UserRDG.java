package dom.model.user.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class UserRDG {
	
	private static final String FIND_ALL = "SELECT id, version, username, password FROM users;";

	private static final String FIND_BY_ID = "SELECT id, version, username, password FROM users WHERE id = ?;";

	private static final String FIND_BY_USERNAME = "SELECT id, version, username, password "
			+ "FROM users WHERE username = ?;";
		
	private static final String INSERT = "INSERT INTO users (id, version, username, password) "
			+ "VALUES (?, ?, ?, ?);";
	
	private static final String UPDATE = "UPDATE users SET username = ?, password = ?, version = (version + 1) "
			+ "WHERE id = ? AND version = ?";
	
	private static final String DELETE = "DELETE FROM users WHERE id = ? AND version = ?;";
	
	private static final String GET_MAX_ID = "SELECT MAX(id) AS max_id FROM users;";
	private static long maxId = 0;
		
	private long id;
	private int version;
	private String username;
	private String password;
	
	public UserRDG(long id, int version, String username, String password) {
		this.id = id;
		this.version = version;
		this.username = username;
		this.password = password;
	}
	
	public long getId() {
		return id;
	}
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public static List<UserRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		
		UserRDG userRDG = null;
		List<UserRDG> userRDGs = new ArrayList<UserRDG>();
		while(rs.next()) {
			userRDG = new UserRDG(
					rs.getLong("id"),
					rs.getInt("version"),
					rs.getString("username"),
					rs.getString("password")
			);
			userRDGs.add(userRDG);
		}
		
		rs.close();
		ps.close();
		
		return userRDGs;
	}
	
	public static UserRDG findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		UserRDG userRDG = null;
		if (rs.next()) {
			userRDG = new UserRDG(
					rs.getLong("id"),
					rs.getInt("version"),
					rs.getString("username"),
					rs.getString("password")
			);
		}
		
		rs.close();
		ps.close();
		
		return userRDG;
	}
	
	public static UserRDG findByUsername(String username) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_USERNAME);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		
		UserRDG userRDG = null;
		if (rs.next()) {
			userRDG = new UserRDG(
					rs.getLong("id"),
					rs.getInt("version"),
					rs.getString("username"),
					rs.getString("password")
			);
		}
		
		rs.close();
		ps.close();
		
		return userRDG;
	}
	
	public int insert() throws SQLException {
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
	
	public int update() throws SQLException {
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
	
	public int delete() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1, id);
		ps.setInt(2, version);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static synchronized long getMaxId() throws SQLException {
		if (maxId == 0) {
			Connection con = DbRegistry.getDbConnection();
			
			PreparedStatement ps = con.prepareStatement(GET_MAX_ID);
			ResultSet rs = ps.executeQuery();
			
			maxId = rs.next() ? rs.getLong("max_id") : 1;
			rs.close();
		}
		
		return ++maxId;
	}

}
