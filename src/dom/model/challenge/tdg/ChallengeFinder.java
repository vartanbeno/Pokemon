package dom.model.challenge.tdg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.challenge.ChallengeStatus;

public class ChallengeFinder {
	
	private static final String FIND_ALL = String.format("SELECT %1$s FROM %2$s;", ChallengeTDG.getColumns(), ChallengeTDG.getTableName());

	private static final String FIND_BY_ID = String.format("SELECT %1$s FROM %2$s WHERE id = ?;", ChallengeTDG.getColumns(), ChallengeTDG.getTableName());
	
	private static final String FIND_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s WHERE challenger = ?;", ChallengeTDG.getColumns(), ChallengeTDG.getTableName());

	private static final String FIND_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s WHERE challengee = ?;", ChallengeTDG.getColumns(), ChallengeTDG.getTableName());
	
	private static final String FIND_OPEN_BY_ID = String.format("SELECT %1$s FROM %2$s WHERE id = ? AND status = %3$d;",
			ChallengeTDG.getColumns(), ChallengeTDG.getTableName(), ChallengeStatus.open.ordinal());
	
	private static final String FIND_OPEN_BY_CHALLENGER = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? AND status = %3$d;", ChallengeTDG.getColumns(), ChallengeTDG.getTableName(), ChallengeStatus.open.ordinal());

	private static final String FIND_OPEN_BY_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challengee = ? AND status = %3$d;", ChallengeTDG.getColumns(), ChallengeTDG.getTableName(), ChallengeStatus.open.ordinal());
	
	private static final String FIND_OPEN_BY_CHALLENGER_AND_CHALLENGEE = String.format("SELECT %1$s FROM %2$s "
			+ "WHERE challenger = ? AND challengee = ? AND status = %3$d;", ChallengeTDG.getColumns(), ChallengeTDG.getTableName(), ChallengeStatus.open.ordinal());
	
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
	
	public static ResultSet findOpenById(long id) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_ID);
		ps.setLong(1, id);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findOpenByChallenger(long challenger) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGER);
		ps.setLong(1, challenger);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findOpenByChallengee(long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGEE);
		ps.setLong(1, challengee);
		
		return ps.executeQuery();
	}
	
	public static ResultSet findOpenByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		Connection con = DbRegistry.getDbConnection();
		
		PreparedStatement ps = con.prepareStatement(FIND_OPEN_BY_CHALLENGER_AND_CHALLENGEE);
		ps.setLong(1, challenger);
		ps.setLong(2, challengee);
		
		return ps.executeQuery();
	}

}
