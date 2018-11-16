package dom.model.card.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.deck.rdg.DeckRDG;

/**
 * 
 * CardTDG: Card Table Data Gateway.
 * Points to the cards table.
 * Provides methods to find, insert, and delete cards.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class CardTDG {
	
	private static final String TABLE_NAME = "cards";
	
	private static final String COLUMNS = "id, deck, type, name";
	
	private static final int CARDS_PER_DECK = 40;
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "deck BIGINT NOT NULL,"
			+ "type VARCHAR(1) NOT NULL,"
			+ "name VARCHAR(30) NOT NULL,"
			+ "PRIMARY KEY (id),"
			+ "FOREIGN KEY (deck) REFERENCES %2$s (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME, DeckRDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", COLUMNS, TABLE_NAME);
		
	private static final String FIND_BY_DECK = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE deck = ? ORDER BY ID;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ?;", TABLE_NAME);
	
	private static final String DELETE_DECK = String.format("DELETE FROM %1$s WHERE deck = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public static int getNumberOfCardsPerDeck() {
		return CARDS_PER_DECK;
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
	
	public static ResultSet findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByDeck(long deck) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_DECK);
		ps.setLong(1, deck);
		
		return ps.executeQuery();
	}
	
	public static int insert(long id, long deck, String type, String name) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, deck);
		ps.setString(3, type);
		ps.setString(4, name);
		
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
	
	public static int deleteAllCardsInDeck(long deck) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE_DECK);
		ps.setLong(1, deck);
		
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
