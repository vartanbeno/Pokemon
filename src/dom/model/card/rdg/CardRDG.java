package dom.model.card.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * Card RDG: Card Row Data Gateway.
 * Points to row(s) in the cards table.
 * Provides methods to find, insert, update, and delete cards.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * @author vartanbeno
 *
 */
public class CardRDG {
	
	private static final String TABLE_NAME = "cards";
	
	private static final String COLUMNS = "id, type, name";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "type VARCHAR(1) NOT NULL,"
			+ "name VARCHAR(30) NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME);
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", COLUMNS, TABLE_NAME);

	private static final String FIND_BY_TYPE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE type = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_NAME = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE name = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_TYPE_AND_NAME = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE type = ? AND name = ?;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET type = ?, name = ? "
			+ "WHERE id = ?;", TABLE_NAME);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ?;", TABLE_NAME);
	
	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	private long id;
	private String type;
	private String name;
	
	public CardRDG(long id, String type, String name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}
	
	public long getId() {
		return id;
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
	
	public static void createTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		ResultSet rs = con.getMetaData().getTables(null, null, TABLE_NAME, null);
		boolean tableExists = rs.next();
		
		Statement s = con.createStatement();
		s.execute(CREATE_TABLE);
		
		if (!tableExists) {
			insertCards();
		}
	}
	
	public static void dropTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		Statement s = con.createStatement();
		s.execute(TRUNCATE_TABLE);
		
		s = con.createStatement();
		s.execute(DROP_TABLE);
	}
	
	private static void insertCards() throws SQLException {
		new CardRDG(getMaxId(), "e", "Fire").insert();
		new CardRDG(getMaxId(), "p", "Charizard").insert();
		new CardRDG(getMaxId(), "p", "Meowth").insert();
		new CardRDG(getMaxId(), "t", "Misty").insert();
		new CardRDG(getMaxId(), "e", "Water").insert();
		new CardRDG(getMaxId(), "t", "Brock").insert();
		new CardRDG(getMaxId(), "p", "Pikachu").insert();
		new CardRDG(getMaxId(), "e", "Electric").insert();
		new CardRDG(getMaxId(), "t", "Ash").insert();
		new CardRDG(getMaxId(), "e", "Grass").insert();
		new CardRDG(getMaxId(), "p", "Blaziken").insert();
		new CardRDG(getMaxId(), "p", "Bulbasaur").insert();
		new CardRDG(getMaxId(), "p", "Magikarp").insert();
		new CardRDG(getMaxId(), "e", "Poison").insert();
		new CardRDG(getMaxId(), "e", "Flying").insert();
		new CardRDG(getMaxId(), "e", "Dragon").insert();
		new CardRDG(getMaxId(), "p", "Groudon").insert();
		new CardRDG(getMaxId(), "p", "Snorlax").insert();
		new CardRDG(getMaxId(), "t", "Jessie").insert();
		new CardRDG(getMaxId(), "t", "James").insert();
		new CardRDG(getMaxId(), "t", "Jessie").insert();
		new CardRDG(getMaxId(), "p", "Onyx").insert();
		new CardRDG(getMaxId(), "p", "Eevee").insert();
		new CardRDG(getMaxId(), "p", "Mew").insert();
		new CardRDG(getMaxId(), "p", "Mewtwo").insert();
	}
	
	public static List<CardRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();
		
		CardRDG cardRDG = null;
		List<CardRDG> cardRDGs = new ArrayList<CardRDG>();
		while (rs.next()) {
			cardRDG = new CardRDG(
					rs.getLong("id"),
					rs.getString("type"),
					rs.getString("name")
			);
			cardRDGs.add(cardRDG);
		}
		
		rs.close();
		ps.close();
		
		return cardRDGs;
	}
	
	public static CardRDG findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();
		
		CardRDG cardRDG = null;
		if (rs.next()) {
			cardRDG = new CardRDG(
					rs.getLong("id"),
					rs.getString("type"),
					rs.getString("name")
			);
		}
		
		rs.close();
		ps.close();
		
		return cardRDG;
	}
	
	public static List<CardRDG> findByType(String type) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_TYPE);
		ps.setString(1, type);
		ResultSet rs = ps.executeQuery();
		
		CardRDG cardRDG = null;
		List<CardRDG> cardRDGs = new ArrayList<CardRDG>();
		while (rs.next()) {
			cardRDG = new CardRDG(
					rs.getLong("id"),
					rs.getString("type"),
					rs.getString("name")
			);
			cardRDGs.add(cardRDG);
		}
		
		rs.close();
		ps.close();
		
		return cardRDGs;
	}
	
	public static List<CardRDG> findByName(String name) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_NAME);
		ps.setString(1, name);
		ResultSet rs = ps.executeQuery();
		
		CardRDG cardRDG = null;
		List<CardRDG> cardRDGs = new ArrayList<CardRDG>();
		while (rs.next()) {
			cardRDG = new CardRDG(
					rs.getLong("id"),
					rs.getString("type"),
					rs.getString("name")
			);
			cardRDGs.add(cardRDG);
		}
		
		rs.close();
		ps.close();
		
		return cardRDGs;
	}
	
	public static List<CardRDG> findByTypeAndName(String type, String name) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_TYPE_AND_NAME);
		ps.setString(1, type);
		ps.setString(2, name);
		ResultSet rs = ps.executeQuery();
		
		CardRDG cardRDG = null;
		List<CardRDG> cardRDGs = new ArrayList<CardRDG>();
		while (rs.next()) {
			cardRDG = new CardRDG(
					rs.getLong("id"),
					rs.getString("type"),
					rs.getString("name")
			);
			cardRDGs.add(cardRDG);
		}
		
		rs.close();
		ps.close();
		
		return cardRDGs;
	}
	
	public int insert() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setString(2, type);
		ps.setString(3, name);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public int update() throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setString(1, type);
		ps.setString(2, name);
		ps.setLong(3, id);
		
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
