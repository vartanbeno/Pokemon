package dom.model.user.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * UserTDG: User Table Data Gateway.
 * Points to the users table.
 * Provides methods to insert, update, and delete users.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class UserTDG {
	
	private static final String TABLE_NAME = "User";
	
	private static final String COLUMNS = "id, version, username, password";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "version BIGINT NOT NULL,"
			+ "username VARCHAR(64) NOT NULL,"
			+ "password VARCHAR(64) NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME);
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
		
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, SHA2(?, 256));", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET username = ?, password = SHA2(?, 256), version = (version + 1) "
			+ "WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public static String getColumns() {
		return COLUMNS;
	}
	
	public static void createTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		Statement s = con.createStatement();
		s.execute(CREATE_TABLE);
		s.close();
	}
	
	public static void dropTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		Statement s = con.createStatement();
		s.execute(TRUNCATE_TABLE);
		
		s = con.createStatement();
		s.execute(DROP_TABLE);
		s.close();
	}
	
	public static int insert(long id, long version, String username, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, version);
		ps.setString(3, username);
		ps.setString(4, password);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int update(long id, long version, String username, String password) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setString(1, username);
		ps.setString(2, password);
		ps.setLong(3, id);
		ps.setLong(4, version);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int delete(long id, long version) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1, id);
		ps.setLong(2, version);
		
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
