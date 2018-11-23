package dom.model.deck.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * Provides methods to find records in the Deck table.
 * 
 * @author vartanbeno
 *
 */
public class DeckFinder {
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", DeckTDG.getColumns(), DeckTDG.getTableName());
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", DeckTDG.getColumns(), DeckTDG.getTableName());
	
	private static final String FIND_BY_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE player = ?;", DeckTDG.getColumns(), DeckTDG.getTableName());
	
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
	
	public static ResultSet findByPlayer(long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_PLAYER);
		ps.setLong(1, player);
		
		return ps.executeQuery();
	}

}
