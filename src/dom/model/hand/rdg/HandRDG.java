package dom.model.hand.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.card.rdg.CardRDG;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.user.rdg.UserRDG;

/**
 * 
 * GameRDG: Hand Row Data Gateway.
 * Points to row(s) in the hands table.
 * Provides methods to find, insert, and delete hands.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class HandRDG {
	
private static final String TABLE_NAME = "hands";
	
	private static final String COLUMNS = "id, game, player, deck, card";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "game BIGINT NOT NULL,"
			+ "player BIGINT NOT NULL,"
			+ "deck BIGINT NOT NULL,"
			+ "card BIGINT NOT NULL,"
			+ "PRIMARY KEY (id),"
			+ "FOREIGN KEY (game) REFERENCES %2$s (id),"
			+ "FOREIGN KEY (player) REFERENCES %3$s (id),"
			+ "FOREIGN KEY (deck) REFERENCES %4$s (id),"
			+ "FOREIGN KEY (card) REFERENCES %5$s (id)"
			+ ") ENGINE=InnoDB;",
			TABLE_NAME, GameRDG.getTableName(), UserRDG.getTableName(), DeckRDG.getTableName(), CardRDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE player = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_GAME = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_GAME_AND_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ? AND player = ?;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	private long id;
	private long game;
	private long player;
	private long deck;
	private long card;
	
	public HandRDG(long id, long game, long player, long deck, long card) {
		this.id = id;
		this.game = game;
		this.player = player;
		this.deck = deck;
		this.card = card;
	}
	
	public long getId() {
		return id;
	}
	
	public long getGame() {
		return game;
	}
	
	public long getPlayer() {
		return player;
	}
	
	public long getDeck() {
		return deck;
	}
	
	public long getCard() {
		return card;
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
	
	public static List<HandRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		
		HandRDG handRDG = null;
		List<HandRDG> handRDGs = new ArrayList<HandRDG>();
		while (rs.next()) {
			handRDG = new HandRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card")
			);
			handRDGs.add(handRDG);
		}
		
		rs.close();
		ps.close();
		
		return handRDGs;
	}
	
	public static HandRDG findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		HandRDG handRDG = null;
		while (rs.next()) {
			handRDG = new HandRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card")
			);
		}
		
		rs.close();
		ps.close();
		
		return handRDG;
	}
	
	public static List<HandRDG> findByPlayer(long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_PLAYER);
		ps.setLong(1, player);
		ResultSet rs = ps.executeQuery();
		
		HandRDG handRDG = null;
		List<HandRDG> handRDGs = new ArrayList<HandRDG>();
		while (rs.next()) {
			handRDG = new HandRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card")
			);
			handRDGs.add(handRDG);
		}
		
		rs.close();
		ps.close();
		
		return handRDGs;
	}
	
	public static List<HandRDG> findByGame(long game) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME);
		ps.setLong(1, game);
		ResultSet rs = ps.executeQuery();
		
		HandRDG handRDG = null;
		List<HandRDG> handRDGs = new ArrayList<HandRDG>();
		while (rs.next()) {
			handRDG = new HandRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card")
			);
			handRDGs.add(handRDG);
		}
		
		rs.close();
		ps.close();
		
		return handRDGs;
	}
	
	public static List<HandRDG> findByGameAndPlayer(long game, long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME_AND_PLAYER);
		ps.setLong(1, game);
		ps.setLong(2, player);
		ResultSet rs = ps.executeQuery();
		
		HandRDG handRDG = null;
		List<HandRDG> handRDGs = new ArrayList<HandRDG>();
		while (rs.next()) {
			handRDG = new HandRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card")
			);
			handRDGs.add(handRDG);
		}
		
		rs.close();
		ps.close();
		
		return handRDGs;
	}
	
	public int insert() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, game);
		ps.setLong(3, player);
		ps.setLong(4, deck);
		ps.setLong(5, card);
		
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
