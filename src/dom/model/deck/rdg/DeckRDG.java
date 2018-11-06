package dom.model.deck.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.user.rdg.UserRDG;

/**
 * 
 * DeckRDG: Deck Row Data Gateway.
 * Points to row(s) in the decks table.
 * Provides methods to find, insert, and delete decks.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class DeckRDG {
	
	private static final String TABLE_NAME = "decks";
	
	private static final String COLUMNS = "id, player";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "player BIGINT NOT NULL,"
			+ "PRIMARY KEY (id),"
			+ "FOREIGN KEY (player) REFERENCES %2$s (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME, UserRDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE player = ?;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ? AND player = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	private long id;
	private long player;
	
	public DeckRDG(long id, long player) {
		this.id = id;
		this.player = player;
	}

	public long getId() {
		return id;
	}

	public long getPlayer() {
		return player;
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
	
	public static List<DeckRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		
		DeckRDG deckRDG = null;
		List<DeckRDG> deckRDGs = new ArrayList<DeckRDG>();
		while (rs.next()) {
			deckRDG = new DeckRDG(rs.getLong("id"), rs.getLong("player"));
			deckRDGs.add(deckRDG);
		}
		
		rs.close();
		ps.close();
		
		return deckRDGs;
	}
	
	public static DeckRDG findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		DeckRDG deckRDG = null;
		if (rs.next()) {
			deckRDG = new DeckRDG(rs.getLong("id"), rs.getLong("player"));
		}
		
		rs.close();
		ps.close();
		
		return deckRDG;
	}
	
	public static DeckRDG findByPlayer(long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_PLAYER);
		ps.setLong(1, player);
		ResultSet rs = ps.executeQuery();
		
		DeckRDG deckRDG = null;
		if (rs.next()) {
			deckRDG = new DeckRDG(rs.getLong("id"), rs.getLong("player"));
		}
		
		rs.close();
		ps.close();
		
		return deckRDG;
	}
	
	public int insert() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, player);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public int delete() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1, id);
		ps.setLong(2, player);
		
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
