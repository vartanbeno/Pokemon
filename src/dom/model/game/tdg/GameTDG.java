package dom.model.game.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.deck.tdg.DeckTDG;
import dom.model.game.GameStatus;
import dom.model.user.tdg.UserTDG;

/**
 * 
 * GameTDG: Game Table Data Gateway.
 * Points to the games table.
 * Provides methods to find, insert, update, and delete games.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * The status column has the following possible values:
 *  - 0: the game is still ongoing.
 *  - 1: the challenger won.
 *  - 2: the challengee won.
 *  - 3: the challenger retired.
 *  - 4: the challengee retired.
 * These are all values from the GameStatus enum.
 * 
 * @author vartanbeno
 *
 */
public class GameTDG {
	
	private static final String TABLE_NAME = "games";
	
	private static final String COLUMNS = "id, version, challenger, challengee, challenger_deck, challengee_deck, status";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "version BIGINT NOT NULL,"
			+ "challenger BIGINT NOT NULL,"
			+ "challengee BIGINT NOT NULL,"
			+ "challenger_deck BIGINT NOT NULL,"
			+ "challengee_deck BIGINT NOT NULL,"
			+ "status INT NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME, UserTDG.getTableName(), DeckTDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_STATUS = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE status = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challengee = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_CHALLENGER_AND_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? AND challengee = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_CHALLENGER_OR_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? OR challengee = ?;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET status = ?, version = (version + 1) "
			+ "WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ? AND version = ?;", TABLE_NAME);
	
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
	
	public static ResultSet findByStatus(int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_STATUS);
		ps.setInt(1, status);
		
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
	
	public static ResultSet findByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER_AND_CHALLENGEE);
		ps.setLong(1, challenger);
		ps.setLong(2, challengee);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByChallengerOrChallengee(long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER_OR_CHALLENGEE);
		ps.setLong(1, player);
		ps.setLong(2, player);
		
		return ps.executeQuery();
	}
	
	public static int insert(long id, long version, long challenger, long challengee, long challengerDeck, long challengeeDeck) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, version);
		ps.setLong(3, challenger);
		ps.setLong(4, challengee);
		ps.setLong(5, challengerDeck);
		ps.setLong(6, challengeeDeck);
		ps.setInt(7, GameStatus.ongoing.ordinal());
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int update(int status, long id, long version) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setInt(1, status);
		ps.setLong(2, id);
		ps.setLong(3, version);
		
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
