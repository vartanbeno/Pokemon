package dom.model.game.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.deck.tdg.DeckTDG;
import dom.model.game.Game;
import dom.model.game.IGame;
import dom.model.game.tdg.GameTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserMapper;
import dom.model.user.tdg.UserTDG;

public class GameMapper {
	
	public static List<IGame> findAll() throws SQLException {
		
		ResultSet rs = GameTDG.findAll();
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static Game findById(long id) throws SQLException {
		
		ResultSet rs = GameTDG.findById(id);
		
		Game game = rs.next() ? buildGame(rs) : null;
		rs.close();
		
		return game;
		
	}
	
	public static List<IGame> findByStatus(int status) throws SQLException {
		
		ResultSet rs = GameTDG.findByStatus(status);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallenger(long challenger) throws SQLException {
		
		ResultSet rs = GameTDG.findByChallenger(challenger);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallengee(long challengee) throws SQLException {
		
		ResultSet rs = GameTDG.findByChallengee(challengee);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		
		ResultSet rs = GameTDG.findByChallengerAndChallengee(challenger, challengee);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallengerOrChallengee(long player) throws SQLException {
		
		ResultSet rs = GameTDG.findByChallengerOrChallengee(player);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static void insert(Game game) throws SQLException {
		GameTDG.insert(
				game.getId(),
				game.getChallenger().getId(),
				game.getChallengee().getId(),
				game.getChallengerDeck().getId(),
				game.getChallengeeDeck().getId()
		);
	}
	
	public static void update(Game game) throws SQLException {
		GameTDG.update(game.getStatus(), game.getId());
	}
	
	public static void delete(Game game) throws SQLException {
		GameTDG.delete(game.getId());
	}
	
	public static Game buildGame(ResultSet rs) throws SQLException {
		
		ResultSet challengerRS = UserTDG.findById(rs.getLong("challenger"));
		User challenger = challengerRS.next() ? UserMapper.buildUser(challengerRS) : null;
		challengerRS.close();
		
		ResultSet challengeeRS = UserTDG.findById(rs.getLong("challengee"));
		User challengee = challengeeRS.next() ? UserMapper.buildUser(challengeeRS) : null;
		challengeeRS.close();
		
		ResultSet challengerDeckRS = DeckTDG.findById(rs.getLong("challenger_deck"));
		Deck challengerDeck = challengerDeckRS.next() ? DeckMapper.buildDeck(challengerDeckRS) : null;
		challengerDeckRS.close();
		
		ResultSet challengeeDeckRS = DeckTDG.findById(rs.getLong("challengee_deck"));
		Deck challengeeDeck = challengeeDeckRS.next() ? DeckMapper.buildDeck(challengeeDeckRS) : null;
		challengeeDeckRS.close();
		
		return new Game(
				rs.getLong("id"),
				challenger,
				challengee,
				challengerDeck,
				challengeeDeck,
				rs.getInt("status")
		);
		
	}
	
	public static List<IGame> buildGames(ResultSet rs) throws SQLException {
		
		List<IGame> games = new ArrayList<IGame>();
		
		while (rs.next()) {
			
			Game game = buildGame(rs);
			games.add(game);
			
		}
		
		rs.close();
		
		return games;
		
	}

}
