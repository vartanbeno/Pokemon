package dom.model.attachedenergy.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

/**
 * 
 * Provides methods to find records in the AttachedEnergy table.
 * 
 * @author vartanbeno
 *
 */
public class AttachedEnergyFinder {
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", AttachedEnergyTDG.getColumns(), AttachedEnergyTDG.getTableName());
		
	private static final String FIND_BY_GAME = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ?;", AttachedEnergyTDG.getColumns(), AttachedEnergyTDG.getTableName());
	
	private static final String FIND_BY_GAME_AND_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ? AND player = ?;", AttachedEnergyTDG.getColumns(), AttachedEnergyTDG.getTableName());
	
	private static final String FIND_BY_GAME_AND_PLAYER_AND_POKEMON_CARD = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ? AND player = ? AND pokemon_card = ?;", AttachedEnergyTDG.getColumns(), AttachedEnergyTDG.getTableName());
	
	private static final String FIND_BY_GAME_AND_GAME_TURN_AND_PLAYER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE game = ? AND game_turn = ? AND player = ?;", AttachedEnergyTDG.getColumns(), AttachedEnergyTDG.getTableName());
	
	public static ResultSet findById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_ID);
		ps.setLong(1, id);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByGame(long game) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME);
		ps.setLong(1, game);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByGameAndPlayer(long game, long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME_AND_PLAYER);
		ps.setLong(1, game);
		ps.setLong(2, player);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByGameAndPlayerAndPokemonCard(long game, long player, long card) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME_AND_PLAYER_AND_POKEMON_CARD);
		ps.setLong(1, game);
		ps.setLong(2, player);
		ps.setLong(3, card);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByGameAndGameTurnAndPlayer(long game, long gameTurn, long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_GAME_AND_GAME_TURN_AND_PLAYER);
		ps.setLong(1, game);
		ps.setLong(2, gameTurn);
		ps.setLong(3, player);
		
		return ps.executeQuery();
	}

}
