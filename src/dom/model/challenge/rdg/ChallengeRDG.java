package dom.model.challenge.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.challenge.ChallengeStatus;
import dom.model.user.rdg.UserRDG;

/**
 * 
 * ChallengeRDG: Challenge Row Data Gateway.
 * Points to row(s) in the challenges table.
 * Provides methods to find, insert, update, and delete challenges.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class ChallengeRDG {
	
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
			+ ") ENGINE=InnoDB;", TABLE_NAME, UserRDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", COLUMNS, TABLE_NAME);

	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s WHERE id = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s WHERE challenger = ?;", COLUMNS, TABLE_NAME);

	private static final String FIND_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s WHERE challengee = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_OPEN_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? AND status = %3$d;", COLUMNS, TABLE_NAME, ChallengeStatus.open.ordinal());

	private static final String FIND_OPEN_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challengee = ? AND status = %3$d;", COLUMNS, TABLE_NAME, ChallengeStatus.open.ordinal());
			
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET status = ? WHERE id = ? AND;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
		
	private long id;
	private long challenger;
	private long challengee;
	private int status;
	
	public ChallengeRDG(long id, long challenger, long challengee, int status) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = status;
	}
	
	public ChallengeRDG(long id, long challenger, long challengee) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.status = ChallengeStatus.open.ordinal();
	}
	
	public long getId() {
		return id;
	}
	
	public long getChallenger() {
		return challenger;
	}

	public void setChallenger(long challenger) {
		this.challenger = challenger;
	}

	public long getChallengee() {
		return challengee;
	}

	public void setChallengee(long challengee) {
		this.challengee = challengee;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
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
	
	public static List<ChallengeRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		
		ChallengeRDG challengeRDG = null;
		List<ChallengeRDG> challengeRDGs = new ArrayList<ChallengeRDG>();
		while(rs.next()) {
			challengeRDG = new ChallengeRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getInt("status")
			);
			challengeRDGs.add(challengeRDG);
		}
		
		rs.close();
		ps.close();
		
		return challengeRDGs;
	}
	
	public static ChallengeRDG findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		ChallengeRDG challengeRDG = null;
		if (rs.next()) {
			challengeRDG = new ChallengeRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getInt("status")
			);
		}
		
		rs.close();
		ps.close();
		
		return challengeRDG;
	}
	
	public static List<ChallengeRDG> findByChallenger(long challenger) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER);
		ps.setLong(1, challenger);
		ResultSet rs = ps.executeQuery();
		
		ChallengeRDG challengeRDG = null;
		List<ChallengeRDG> challengeRDGs = new ArrayList<ChallengeRDG>();
		if (rs.next()) {
			challengeRDG = new ChallengeRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getInt("status")
			);
			challengeRDGs.add(challengeRDG);
		}
		
		rs.close();
		ps.close();
		
		return challengeRDGs;
	}
	
	public static List<ChallengeRDG> findByChallengee(long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGEE);
		ps.setLong(1, challengee);
		ResultSet rs = ps.executeQuery();
		
		ChallengeRDG challengeRDG = null;
		List<ChallengeRDG> challengeRDGs = new ArrayList<ChallengeRDG>();
		if (rs.next()) {
			challengeRDG = new ChallengeRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getInt("status")
			);
			challengeRDGs.add(challengeRDG);
		}
		
		rs.close();
		ps.close();
		
		return challengeRDGs;
	}
	
	public static List<ChallengeRDG> findOpenByChallenger(long challenger) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGER);
		ps.setLong(1, challenger);
		ResultSet rs = ps.executeQuery();
		
		ChallengeRDG challengeRDG = null;
		List<ChallengeRDG> challengeRDGs = new ArrayList<ChallengeRDG>();
		if (rs.next()) {
			challengeRDG = new ChallengeRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getInt("status")
			);
			challengeRDGs.add(challengeRDG);
		}
		
		rs.close();
		ps.close();
		
		return challengeRDGs;
	}
	
	public static List<ChallengeRDG> findOpenByChallengee(long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGEE);
		ps.setLong(1, challengee);
		ResultSet rs = ps.executeQuery();
		
		ChallengeRDG challengeRDG = null;
		List<ChallengeRDG> challengeRDGs = new ArrayList<ChallengeRDG>();
		if (rs.next()) {
			challengeRDG = new ChallengeRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getInt("status")
			);
			challengeRDGs.add(challengeRDG);
		}
		
		rs.close();
		ps.close();
		
		return challengeRDGs;
	}
	
	public int insert() throws SQLException {
		
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
	
	public int update() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setInt(1, status);
		ps.setLong(2, id);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public int delete() throws SQLException {
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
		}
		
		return ++maxId;
	}

}
