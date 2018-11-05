package dom.model.cardsindeck.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.card.rdg.CardRDG;
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
	
	private static final String COLUMNS = "deck, card";
	
	private static final int CARDS_PER_DECK = 40;
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "deck BIGINT NOT NULL,"
			+ "card BIGINT NOT NULL,"
			+ "FOREIGN KEY (deck) REFERENCES %2$s (id),"
			+ "FOREIGN KEY (card) REFERENCES %3$s (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME, DeckRDG.getTableName(), CardRDG.getTableName());
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
		
	private static final String FIND_BY_DECK = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE deck = ?;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE deck = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	private long deck;
	private long card;
	
	public CardsInDeckRDG(long deck, long card) {
		this.deck = deck;
		this.card = card;
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
	
	public static List<CardsInDeckRDG> findByDeck(long deck) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_DECK);
		ps.setLong(1, deck);
		ResultSet rs = ps.executeQuery();
		
		CardsInDeckRDG cardsInDeckRDG = null;
		List<CardsInDeckRDG> cardsInDeckRDGs = new ArrayList<CardsInDeckRDG>();
		if (rs.next()) {
			cardsInDeckRDG = new CardsInDeckRDG(rs.getLong("deck"), rs.getLong("card"));
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
		ps.setLong(2, card);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public int delete() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(DELETE);
		ps.setLong(1, deck);
		ps.setLong(2, card);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static void createDeck(long deck) throws SQLException {		
		List<CardRDG> cards = CardRDG.findAll();
		
		Random random = new Random();
		for (int i = 0; i < CARDS_PER_DECK; i++) {
			CardRDG cardRDG = cards.get(random.nextInt(cards.size()));
			new CardsInDeckRDG(deck, cardRDG.getId()).insert();
		}
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
