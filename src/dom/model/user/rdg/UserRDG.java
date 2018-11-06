package dom.model.user.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * UserRDG: User Row Data Gateway.
 * Points to row(s) in the users table.
 * Provides methods to find, insert, update, and delete users.
 * 
 * Usually bad practice to have create/truncate/drop queries in an RDG, but...
 * Let's do this!
 * 
 * @author vartanbeno
 *
 */
public class UserRDG {
	
	private static final String TABLE_NAME = "users";
	
	private static final String COLUMNS = "id, version, username, password";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "version INT NOT NULL,"
			+ "username VARCHAR(30) NOT NULL,"
			+ "password VARCHAR(64) NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME);
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", COLUMNS, TABLE_NAME);

	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s WHERE id = ?;", COLUMNS, TABLE_NAME);

	private static final String FIND_BY_USERNAME = String.format("SELECT %1$s FROM %2$s WHERE username = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_USERNAME_AND_PASSWORD = String.format("SELECT %1$s "
			+ "FROM %2$s WHERE username = ? AND password = SHA2(?, 256);", COLUMNS, TABLE_NAME);
		
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, SHA2(?, 256));", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET username = ?, password = SHA2(?, 256), version = (version + 1) "
			+ "WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
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
	
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public static void createTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		Statement s = con.createStatement();
		s.execute(CREATE_TABLE);
	}
	
	public static void dropTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		Statement s = con.createStatement();
		s.execute(TRUNCATE_TABLE);
		
		s = con.createStatement();
		s.execute(DROP_TABLE);
	}
	
	public static List<UserRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		
		UserRDG userRDG = null;
		List<UserRDG> userRDGs = new ArrayList<UserRDG>();
		while (rs.next()) {
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
	
	public static UserRDG findByUsernameAndPassword(String username, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_USERNAME_AND_PASSWORD);
		ps.setString(1, username);
		ps.setString(2, password);
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
			ps.close();
		}
		
		return ++maxId;
	}

}
