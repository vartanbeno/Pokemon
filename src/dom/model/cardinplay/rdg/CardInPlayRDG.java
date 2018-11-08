package dom.model.cardinplay.rdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.card.rdg.CardRDG;
import dom.model.cardinplay.CardStatus;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.user.rdg.UserRDG;

/**
*
* CardNotInRDG: Card In Play Row Data Gateway.
* Points to row(s) in the cards_in_play table.
* Provides methods to find, insert, and delete cards in play.
*
* Also includes create/truncate/drop queries.
* 
* A card in play is a card that has been drawn from the deck.
* It is either in the player's hand, benched, or discarded.
* We use the CardStatus enum to distinguish between them.
*
* @author vartanbeno
*
*/
public class CardInPlayRDG {
	
	private static final String TABLE_NAME = "cards_in_play";

	private static final String COLUMNS = "id, game, player, deck, card, status";

	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "game BIGINT NOT NULL,"
			+ "player BIGINT NOT NULL,"
			+ "deck BIGINT NOT NULL,"
			+ "card BIGINT NOT NULL,"
			+ "status INT NOT NULL,"
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
	
	private static final String FIND_BY_GAME_AND_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ? AND player = ?;", COLUMNS, TABLE_NAME);
	
	private static final String FIND_BY_GAME_AND_PLAYER_AND_STATUS = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ? AND player = ? AND status = ?;", COLUMNS, TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET status = ? WHERE id = ?;", TABLE_NAME, COLUMNS);
	
	private static final String DELETE = String.format("DELETE FROM %1$s WHERE id = ?;", TABLE_NAME);

	private static final String GET_MAX_ID = String.format("SELECT MAX(id) AS max_id FROM %1$s;", TABLE_NAME);
	private static long maxId = 0;
	
	private long id;
	private long game;
	private long player;
	private long deck;
	private long card;
	private int status;
	
	public CardInPlayRDG(long id, long game, long player, long deck, long card, int status) {
		this.id = id;
		this.game = game;
		this.player = player;
		this.deck = deck;
		this.card = card;
		this.status = status;
	}
	
	public CardInPlayRDG(long id, long game, long player, long deck, long card) {
		this.id = id;
		this.game = game;
		this.player = player;
		this.deck = deck;
		this.card = card;
		this.status = CardStatus.hand.ordinal();
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public static List<CardInPlayRDG> findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		ResultSet rs = ps.executeQuery();

		CardInPlayRDG cardInPlayRDG = null;
		List<CardInPlayRDG> cardInPlayRDGs = new ArrayList<CardInPlayRDG>();
		while (rs.next()) {
			cardInPlayRDG = new CardInPlayRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card"),
					rs.getInt("status")
			);
			cardInPlayRDGs.add(cardInPlayRDG);
		}

		rs.close();
		ps.close();

		return cardInPlayRDGs;
	}
	
	public static CardInPlayRDG findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		ResultSet rs = ps.executeQuery();

		CardInPlayRDG cardInPlayRDG = null;
		if (rs.next()) {
			cardInPlayRDG = new CardInPlayRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card"),
					rs.getInt("status")
			);
		}

		rs.close();
		ps.close();

		return cardInPlayRDG;
	}
	
	public static List<CardInPlayRDG> findByGameAndPlayer(long game, long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME_AND_PLAYER);
		ps.setLong(1, game);
		ps.setLong(2, player);
		ResultSet rs = ps.executeQuery();

		CardInPlayRDG cardInPlayRDG = null;
		List<CardInPlayRDG> cardInPlayRDGs = new ArrayList<CardInPlayRDG>();
		while (rs.next()) {
			cardInPlayRDG = new CardInPlayRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card"),
					rs.getInt("status")
			);
			cardInPlayRDGs.add(cardInPlayRDG);
		}

		rs.close();
		ps.close();

		return cardInPlayRDGs;
	}
	
	public static List<CardInPlayRDG> findByGameAndPlayerAndStatus(long game, long player, int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME_AND_PLAYER_AND_STATUS);
		ps.setLong(1, game);
		ps.setLong(2, player);
		ps.setLong(3, status);
		ResultSet rs = ps.executeQuery();

		CardInPlayRDG cardInPlayRDG = null;
		List<CardInPlayRDG> cardInPlayRDGs = new ArrayList<CardInPlayRDG>();
		while (rs.next()) {
			cardInPlayRDG = new CardInPlayRDG(
					rs.getLong("id"),
					rs.getLong("game"),
					rs.getLong("player"),
					rs.getLong("deck"),
					rs.getLong("card"),
					rs.getInt("status")
			);
			cardInPlayRDGs.add(cardInPlayRDG);
		}

		rs.close();
		ps.close();

		return cardInPlayRDGs;
	}
	
	public int insert() throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, game);
		ps.setLong(3, player);
		ps.setLong(4, deck);
		ps.setLong(5, card);
		ps.setInt(6, CardStatus.hand.ordinal());

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
