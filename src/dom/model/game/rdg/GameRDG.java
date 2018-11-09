package dom.model.game.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.deck.rdg.DeckRDG;
import dom.model.game.GameStatus;
import dom.model.user.rdg.UserRDG;

/**
 * 
 * GameRDG: Game Row Data Gateway.
 * Points to row(s) in the games table.
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
 * 
 * @author vartanbeno
 *
 */
public class GameRDG {
	
	private static final String TABLE_NAME = "games";
	
	private static final String COLUMNS = "id, challenger, challengee, challenger_deck, challengee_deck, status";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "challenger BIGINT NOT NULL,"
			+ "challengee BIGINT NOT NULL,"
			+ "challenger_deck BIGINT NOT NULL,"
			+ "challengee_deck BIGINT NOT NULL,"
			+ "status INT NOT NULL,"
			+ "PRIMARY KEY (id),"
			+ "FOREIGN KEY (challenger) REFERENCES %2$s (id),"
			+ "FOREIGN KEY (challengee) REFERENCES %2$s (id),"
			+ "FOREIGN KEY (challenger_deck) REFERENCES %3$s (id),"
			+ "FOREIGN KEY (challengee_deck) REFERENCES %3$s (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME, UserRDG.getTableName(), DeckRDG.getTableName());
	
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
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET status = ? WHERE id = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	private long id;
	private long challenger;
	private long challengee;
	private long challengerDeck;
	private long challengeeDeck;
	private int status;
	
	public GameRDG(
			long id,
			long challenger, long challengee,
			long challengerDeck, long challengeeDeck,
			int status
	) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
		this.status = status;
	}
	
	public GameRDG(
			long id,
			long challenger, long challengee,
			long challengerDeck, long challengeeDeck
	) {
		this.id = id;
		this.challenger = challenger;
		this.challengee = challengee;
		this.challengerDeck = challengerDeck;
		this.challengeeDeck = challengeeDeck;
	}
	
	public long getId() {
		return id;
	}
	
	public long getChallenger() {
		return challenger;
	}
	
	public long getChallengee() {
		return challengee;
	}
	
	public long getChallengerDeck() {
		return challengerDeck;
	}
	
	public long getChallengeeDeck() {
		return challengeeDeck;
	}
	
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	
	public static List<GameRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		
		GameRDG gameRDG = null;
		List<GameRDG> gameRDGs = new ArrayList<GameRDG>();
		while (rs.next()) {
			gameRDG = new GameRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getLong("challenger_deck"),
					rs.getLong("challengee_deck"),
					rs.getInt("status")
			);
			gameRDGs.add(gameRDG);
		}
		
		rs.close();
		ps.close();
		
		return gameRDGs;
	}
	
	public static GameRDG findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		GameRDG gameRDG = null;
		if (rs.next()) {
			gameRDG = new GameRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getLong("challenger_deck"),
					rs.getLong("challengee_deck"),
					rs.getInt("status")
			);
		}
		
		rs.close();
		ps.close();
		
		return gameRDG;
	}
	
	public static List<GameRDG> findByStatus(int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_STATUS);
		ps.setInt(1, status);
		ResultSet rs = ps.executeQuery();
		
		GameRDG gameRDG = null;
		List<GameRDG> gameRDGs = new ArrayList<GameRDG>();
		while (rs.next()) {
			gameRDG = new GameRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getLong("challenger_deck"),
					rs.getLong("challengee_deck"),
					rs.getInt("status")
			);
			gameRDGs.add(gameRDG);
		}
		
		rs.close();
		ps.close();
		
		return gameRDGs;
	}
	
	public static List<GameRDG> findByChallenger(long challenger) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER);
		ps.setLong(1, challenger);
		ResultSet rs = ps.executeQuery();
		
		GameRDG gameRDG = null;
		List<GameRDG> gameRDGs = new ArrayList<GameRDG>();
		while (rs.next()) {
			gameRDG = new GameRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getLong("challenger_deck"),
					rs.getLong("challengee_deck"),
					rs.getInt("status")
			);
			gameRDGs.add(gameRDG);
		}
		
		rs.close();
		ps.close();
		
		return gameRDGs;
	}
	
	public static List<GameRDG> findByChallengee(long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGEE);
		ps.setLong(1, challengee);
		ResultSet rs = ps.executeQuery();
		
		GameRDG gameRDG = null;
		List<GameRDG> gameRDGs = new ArrayList<GameRDG>();
		while (rs.next()) {
			gameRDG = new GameRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getLong("challenger_deck"),
					rs.getLong("challengee_deck"),
					rs.getInt("status")
			);
			gameRDGs.add(gameRDG);
		}
		
		rs.close();
		ps.close();
		
		return gameRDGs;
	}
	
	public static GameRDG findByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER_AND_CHALLENGEE);
		ps.setLong(1, challenger);
		ps.setLong(2, challengee);
		ResultSet rs = ps.executeQuery();
		
		GameRDG gameRDG = null;
		if (rs.next()) {
			gameRDG = new GameRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getLong("challenger_deck"),
					rs.getLong("challengee_deck"),
					rs.getInt("status")
			);
		}
		
		rs.close();
		ps.close();
		
		return gameRDG;
	}
	
	public static List<GameRDG> findByChallengerOrChallengee(long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER_OR_CHALLENGEE);
		ps.setLong(1, player);
		ps.setLong(2, player);
		ResultSet rs = ps.executeQuery();
		
		GameRDG gameRDG = null;
		List<GameRDG> gameRDGs = new ArrayList<GameRDG>();
		if (rs.next()) {
			gameRDG = new GameRDG(
					rs.getLong("id"),
					rs.getLong("challenger"),
					rs.getLong("challengee"),
					rs.getLong("challenger_deck"),
					rs.getLong("challengee_deck"),
					rs.getInt("status")
			);
			gameRDGs.add(gameRDG);
		}
		
		rs.close();
		ps.close();
		
		return gameRDGs;
	}
	
	public int insert() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, challenger);
		ps.setLong(3, challengee);
		ps.setLong(4, challengerDeck);
		ps.setLong(5, challengeeDeck);
		ps.setInt(6, GameStatus.ongoing.ordinal());
		
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
			ps.close();
		}
		
		return ++maxId;
	}

}
