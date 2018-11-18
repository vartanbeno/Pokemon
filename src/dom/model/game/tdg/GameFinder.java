package dom.model.game.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

public class GameFinder {
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", GameTDG.getColumns(), GameTDG.getTableName());
	
	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE id = ?;", GameTDG.getColumns(), GameTDG.getTableName());
	
	private static final String FIND_BY_STATUS = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE status = ?;", GameTDG.getColumns(), GameTDG.getTableName());
	
	private static final String FIND_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ?;", GameTDG.getColumns(), GameTDG.getTableName());
	
	private static final String FIND_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challengee = ?;", GameTDG.getColumns(), GameTDG.getTableName());
	
	private static final String FIND_BY_CHALLENGER_AND_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? AND challengee = ?;", GameTDG.getColumns(), GameTDG.getTableName());
	
	private static final String FIND_BY_CHALLENGER_OR_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? OR challengee = ?;", GameTDG.getColumns(), GameTDG.getTableName());
	
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
	
	public static ResultSet findByStatus(int status) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_STATUS);
		ps.setInt(1, status);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByChallenger(long challenger) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER);
		ps.setLong(1, challenger);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByChallengee(long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGEE);
		ps.setLong(1, challengee);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER_AND_CHALLENGEE);
		ps.setLong(1, challenger);
		ps.setLong(2, challengee);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findByChallengerOrChallengee(long player) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_BY_CHALLENGER_OR_CHALLENGEE);
		ps.setLong(1, player);
		ps.setLong(2, player);
		
		return ps.executeQuery();
	}

}
