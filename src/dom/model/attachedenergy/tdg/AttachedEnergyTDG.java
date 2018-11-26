package dom.model.attachedenergy.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * AttachedEnergyTDG: Attached Energy Table Data Gateway.
 * Points to the AttachedEnergy table.
 * Provides methods to insert, update, and delete attached energy cards.
 * 
 * Also includes create/truncate/drop queries.
 * 
 * Pokemon on the bench can have energy cards attached to them.
 * Each row in this table represents one energy card, attached to a certain Pokemon card in a game.
 * 
 * The energy_card attribute refers to a card in the Card table.
 * The pokemon_card attribute refered to a card in the Bench table.
 * 
 * Since a player can only play one energy per turn, we keep track of this in the game_turn column.
 * When a player plays an energy card, the game's current turn is stored in the game_turn attribute.
 * If they attempt to play another energy card in the same turn (i.e. the same game version), they should get an error.
 * 
 * @author vartanbeno
 *
 */
public class AttachedEnergyTDG {
	
	private static final String TABLE_NAME = "AttachedEnergy";
	
	private static final String COLUMNS = "id, version, game, game_turn, player, energy_card, pokemon_card";
	
	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "version BIGINT NOT NULL,"
			+ "game BIGINT NOT NULL,"
			+ "game_turn BIGINT NOT NULL,"
			+ "player BIGINT NOT NULL,"
			+ "energy_card BIGINT NOT NULL,"
			+ "pokemon_card BIGINT NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;", TABLE_NAME);
	
	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);
	
	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET energy_card = ?, pokemon_card = ?, version = (version + 1) "
			+ "WHERE id = ? AND version = ?;", TABLE_NAME);
	
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
	
	public static int insert(long id, long version, long game, long gameTurn, long player, long energyCard, long pokemonCard) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, version);
		ps.setLong(3, game);
		ps.setLong(4, gameTurn);
		ps.setLong(5, player);
		ps.setLong(6, energyCard);
		ps.setLong(7, pokemonCard);
		
		int result = ps.executeUpdate();
		ps.close();
		
		return result;
	}
	
	public static int update(long id, long version, long energyCard, long pokemonCard) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setLong(1, energyCard);
		ps.setLong(2, pokemonCard);
		ps.setLong(3, id);
		ps.setLong(4, version);
		
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
