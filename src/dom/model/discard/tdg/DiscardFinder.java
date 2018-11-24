package dom.model.discard.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * Provides methods to find records in the Discard table.
 * 
 * @author vartanbeno
 *
 */
public class DiscardFinder {

	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", DiscardTDG.getColumns(), DiscardTDG.getTableName());
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", DiscardTDG.getColumns(), DiscardTDG.getTableName());
	
	private static final String FIND_BY_GAME_AND_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ? AND player = ?;", DiscardTDG.getColumns(), DiscardTDG.getTableName());

	public static ResultSet findAll() throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(FIND_ALL);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByGameAndPlayer(long game, long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();

		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME_AND_PLAYER);
		ps.setLong(1, game);
		ps.setLong(2, player);
		
		return ps.executeQuery();
	}

}
