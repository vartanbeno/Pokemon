package dom.model.card.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * CardTDG: Card Table Data Gateway.
 * Points to the Card table.
 * Provides methods to insert, update, and delete cards.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * A card's basic attribute is its basic form, i.e. Charmeleon's basic form is Charmander
 * because Charmander evolves into Charmeleon.
 * 
 * @author vartanbeno
 *
 */
public class CardTDG {
	
	private static final String TABLE_NAME = "Card";
	
	private static final String COLUMNS = "id, version, deck, type, name, basic";
	
	private static final int CARDS_PER_DECK = 40;
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "version BIGINT NOT NULL,"
			+ "deck BIGINT NOT NULL,"
			+ "type VARCHAR(1) NOT NULL,"
			+ "name VARCHAR(30) NOT NULL,"
			+ "basic VARCHAR(30) NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME);
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET type = ?, name = ?, basic = ?, version = (version + 1) "
			+ "WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ? AND version = ?;", TABLE_NAME);
	
	private static final String DELETE_DECK = String.format("DELETE FROM %1$s WHERE deck = ? AND version = ?;", TABLE_NAME);
	
	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public static String getColumns() {
		return COLUMNS;
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
	
	public static int insert(long id, long version, long deck, String type, String name, String basic) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, version);
		ps.setLong(3, deck);
		ps.setString(4, type);
		ps.setString(5, name);
		ps.setString(6, basic);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int update(long id, long version, String type, String name, String basic) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setString(1, type);
		ps.setString(2, name);
		ps.setString(3, basic);
		ps.setLong(4, id);
		ps.setLong(5, version);
		
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
	
	public static int deleteAllCardsInDeck(long deck) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE_DECK);
		ps.setLong(1, deck);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static synchronized long getMaxId() throws SQLException {
		return UniqueIdFactory.getMaxId(TABLE_NAME, "id");
	}

}
