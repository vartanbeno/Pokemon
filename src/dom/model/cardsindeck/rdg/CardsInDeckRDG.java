package dom.model.cardsindeck.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.deck.rdg.DeckRDG;

/**
 * 
 * CardsInDeckRDG: Cards in Deck Row Data Gateway.
 * Points to row(s) in the cards_in_deck table.
 * Provides methods to find, insert, and delete cards_in_deck rows.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class CardsInDeckRDG {
	
	private static final String TABLE_NAME = "cards_in_deck";
	
	private static final String COLUMNS = "deck, type, name";
	
	private static final int CARDS_PER_DECK = 40;
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "deck BIGINT NOT NULL,"
			+ "type VARCHAR(1) NOT NULL,"
			+ "name VARCHAR(30) NOT NULL,"
			+ "FOREIGN KEY (deck) REFERENCES %2$s (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME, DeckRDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
		
	private static final String FIND_BY_DECK = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE deck = ?;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE deck = ?;", TABLE_NAME);
	
	private long deck;
	private String type;
	private String name;
	
	public CardsInDeckRDG(long deck, String type, String name) {
		this.deck = deck;
		this.type = type;
		this.name = name;
	}
	
	public long getDeck() {
		return deck;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String getTableName() {
		return TABLE_NAME;
	}
	
	public static int getCardsPerDeck() {
		return CARDS_PER_DECK;
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
	
	public static List<CardsInDeckRDG> findByDeck(long deck) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_DECK);
		ps.setLong(1, deck);
		ResultSet rs = ps.executeQuery();
		
		CardsInDeckRDG cardsInDeckRDG = null;
		List<CardsInDeckRDG> cardsInDeckRDGs = new ArrayList<CardsInDeckRDG>();
		while (rs.next()) {
			cardsInDeckRDG = new CardsInDeckRDG(
					rs.getLong("deck"),
					rs.getString("type"),
					rs.getString("name")
			);
			cardsInDeckRDGs.add(cardsInDeckRDG);
		}
		
		rs.close();
		ps.close();
		
		return cardsInDeckRDGs;
	}
	
	public int insert() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, deck);
		ps.setString(2, type);
		ps.setString(3, name);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public int delete() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1, deck);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
}
