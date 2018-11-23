package dom.model.card.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * Provides methods to find records in the Card table.
 * 
 * @author vartanbeno
 *
 */
public class CardFinder {
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", CardTDG.getColumns(), CardTDG.getTableName());
		
	private static final String FIND_BY_DECK = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE deck = ? ORDER BY ID;", CardTDG.getColumns(), CardTDG.getTableName());
	
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

}
