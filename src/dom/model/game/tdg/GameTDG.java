package dom.model.game.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.game.GameStatus;

/**
 * 
 * GameTDG: Game Table Data Gateway.
 * Points to the Game table.
 * Provides methods to insert, update, and delete games.
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
 * The current_turn column represents whose turn it is currently. It's player's ID.
 * The first turn is always the challenger's. When a game first starts, the challenger draws a card, ending their turn.
 * So technically, is it the challengee's turn first? :o
 * 
 * The turn columns represents the nth turn that the game is currently on.
 * When a game starts, it's turn 1. Each time a player ends their turn, it gets incremented by 1.
 * 
 * @author vartanbeno
 *
 */
public class GameTDG {
	
	private static final String TABLE_NAME = "Game";
	
	private static final String COLUMNS = "id, version, challenger, challengee, challenger_deck, challengee_deck, current_turn, turn, status";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "version BIGINT NOT NULL,"
			+ "challenger BIGINT NOT NULL,"
			+ "challengee BIGINT NOT NULL,"
			+ "challenger_deck BIGINT NOT NULL,"
			+ "challengee_deck BIGINT NOT NULL,"
			+ "current_turn BIGINT NOT NULL,"
			+ "turn BIGINT NOT NULL,"
			+ "status INT NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME);
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET current_turn = ?, turn = ?, status = ?, version = (version + 1) "
			+ "WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String RETIRE = String.format("UPDATE %1$s SET status = ?, version = (version + 1) "
			+ "WHERE id = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ? AND version = ?;", TABLE_NAME);
	
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
	
	public static int insert(
			long id, long version, long challenger, long challengee, long challengerDeck, long challengeeDeck, long currentTurn, long turn
	) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, version);
		ps.setLong(3, challenger);
		ps.setLong(4, challengee);
		ps.setLong(5, challengerDeck);
		ps.setLong(6, challengeeDeck);
		ps.setLong(7, currentTurn);
		ps.setLong(8, turn);
		ps.setInt(9, GameStatus.ongoing.ordinal());
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int update(long id, long version, long currentTurn, long turn, int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setLong(1, currentTurn);
		ps.setLong(2, turn);
		ps.setInt(3, status);
		ps.setLong(4, id);
		ps.setLong(5, version);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int retire(long id, int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(RETIRE);
		ps.setInt(1, status);
		ps.setLong(2, id);
		
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
		return UniqueIdFactory.getMaxId(TABLE_NAME, "id");
	}

}
