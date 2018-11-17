package dom.model.challenge.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.challenge.ChallengeStatus;
import dom.model.user.tdg.UserTDG;

/**
 * 
 * ChallengeTDG: Challenge Table Data Gateway.
 * Points to the challenges table.
 * Provides methods to find, insert, update, and delete challenges.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class ChallengeTDG {
	
	private static final String TABLE_NAME = "challenges";
	
	private static final String COLUMNS = "id, challenger, challengee, status";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "challenger BIGINT NOT NULL,"
			+ "challengee BIGINT NOT NULL,"
			+ "status INT NOT NULL,"
			+ "PRIMARY KEY (id),"
			+ "FOREIGN KEY (challenger) REFERENCES %2$s (id),"
			+ "FOREIGN KEY (challengee) REFERENCES %2$s (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME, UserTDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", COLUMNS, TABLE_NAME);

	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s WHERE id = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s WHERE challenger = ?;", COLUMNS, TABLE_NAME);

	private static final String FIND_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s WHERE challengee = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_OPEN_BY_ID = String.format("SELECT %1$s FROM %2$s WHERE id = ? AND status = %3$d;",
			COLUMNS, TABLE_NAME, ChallengeStatus.open.ordinal());
	
	private static final String FIND_OPEN_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? AND status = %3$d;", COLUMNS, TABLE_NAME, ChallengeStatus.open.ordinal());

	private static final String FIND_OPEN_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challengee = ? AND status = %3$d;", COLUMNS, TABLE_NAME, ChallengeStatus.open.ordinal());
	
	private static final String FIND_OPEN_BY_CHALLENGER_AND_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? AND challengee = ? AND status = %3$d;", COLUMNS, TABLE_NAME, ChallengeStatus.open.ordinal());
			
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET status = ? WHERE id = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	public static String getTableName() {
		return TABLE_NAME;
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
	
	public static ResultSet findByChallenger(long challenger) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER);
		ps.setLong(1, challenger);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByChallengee(long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGEE);
		ps.setLong(1, challengee);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findOpenById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_ID);
		ps.setLong(1, id);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findOpenByChallenger(long challenger) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGER);
		ps.setLong(1, challenger);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findOpenByChallengee(long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGEE);
		ps.setLong(1, challengee);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findOpenByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGER_AND_CHALLENGEE);
		ps.setLong(1, challenger);
		ps.setLong(2, challengee);
		
		return ps.executeQuery();
	}
	
	public static int insert(long id, long challenger, long challengee) throws SQLException {
		
		if (challenger == challengee) {
			System.err.println("The challenger and challengee cannot be the same.\n"
					+ "One cannot challenge him/herself.");
			
			return 0;
		}
		
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, challenger);
		ps.setLong(3, challengee);
		ps.setInt(4, ChallengeStatus.open.ordinal());
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int update(int status, long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setInt(1, status);
		ps.setLong(2, id);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int delete(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1, id);
		
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
