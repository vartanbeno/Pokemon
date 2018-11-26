package dom.model.bench.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.dsrg.soenea.service.UniqueIdFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
*
* BenchTDG: Bench Table Data Gateway.
* Points to the Bench table.
* Provides methods to insert, update, and delete cards (Pokemon) that are on a player's bench.
*
* Also includes create/truncate/drop queries.
*
* @author vartanbeno
*
*/
public class BenchTDG {
	
	private static final String TABLE_NAME = "Bench";

	private static final String COLUMNS = "id, version, game, player, deck, card, predecessor";

	private static final String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %1$s("
			+ "id BIGINT NOT NULL,"
			+ "version BIGINT NOT NULL,"
			+ "game BIGINT NOT NULL,"
			+ "player BIGINT NOT NULL,"
			+ "deck BIGINT NOT NULL,"
			+ "card BIGINT NOT NULL,"
			+ "predecessor BIGINT NOT NULL,"
			+ "PRIMARY KEY (id)"
			+ ") ENGINE=InnoDB;",
			TABLE_NAME);

	private static final String TRUNCATE_TABLE = String.format("TRUNCATE TABLE %1$s;", TABLE_NAME);

	private static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %1$s;", TABLE_NAME);
	
	private static final String INSERT = String.format("INSERT INTO %1$s (%2$s) VALUES (?, ?, ?, ?, ?, ?, ?);", TABLE_NAME, COLUMNS);
	
	private static final String UPDATE = String.format("UPDATE %1$s SET card = ?, predecessor = ?, version = (version + 1) "
			+ "WHERE id = ? AND version = ?;", TABLE_NAME, COLUMNS);
	
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
	}

	public static void dropTable() throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		Statement s = con.createStatement();
		s.execute(TRUNCATE_TABLE);

		s = con.createStatement();
		s.execute(DROP_TABLE);
	}
	
	public static int insert(long id, long version, long game, long player, long deck, long card, long predecessor) throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(INSERT);
		ps.setLong(1, id);
		ps.setLong(2, version);
		ps.setLong(3, game);
		ps.setLong(4, player);
		ps.setLong(5, deck);
		ps.setLong(6, card);
		ps.setLong(7, predecessor);

		int result = ps.executeUpdate();
		ps.close();

		return result;
	}
	
	public static int update(long id, long version, long card, long predecessor) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(UPDATE);
		ps.setLong(1, card);
		ps.setLong(2, predecessor);
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
