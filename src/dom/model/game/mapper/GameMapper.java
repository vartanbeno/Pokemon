package dom.model.game.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.dsrg.soenea.domain.mapper.LostUpdateException;

import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayMapper;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.IGame;
import dom.model.game.tdg.GameTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserMapper;

public class GameMapper extends GenericOutputMapper<Long, Game> {
	
	@Override
	public void insert(Game game) throws MapperException {
		try {
			insertStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(Game game) throws MapperException {
		try {
			updateStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public void updateVersion(Game game) throws MapperException {
		try {
			updateVersionStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(Game game) throws MapperException {
		try {
			deleteStatic(game);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(Game game) throws SQLException {
		GameTDG.insert(
				game.getId(),
				game.getVersion(),
				game.getChallenger().getId(),
				game.getChallengee().getId(),
				game.getChallengerDeck().getId(),
				game.getChallengeeDeck().getId()
		);
	}
	
	public static void updateStatic(Game game) throws SQLException, LostUpdateException {
		int count = GameTDG.update(game.getId(), game.getVersion(), game.getStatus());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update game with id: %d.", game.getId()));
	}
	
	public static void updateVersionStatic(Game game) throws SQLException, LostUpdateException {
		int count = GameTDG.updateVersion(game.getId(), game.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot update game with id: %d.", game.getId()));
	}
	
	public static void deleteStatic(Game game) throws SQLException, LostUpdateException {
		int count = GameTDG.delete(game.getId(), game.getVersion());
		if (count == 0) throw new LostUpdateException(String.format("Cannot delete game with id: %d.", game.getId()));
	}
	
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
	
	public static Game buildGame(ResultSet rs) throws SQLException {
		
		User challenger = UserMapper.findById(rs.getLong("challenger"));
		User challengee = UserMapper.findById(rs.getLong("challengee"));
		Deck challengerDeck = DeckMapper.findById(rs.getLong("challenger_deck"));
		Deck challengeeDeck = DeckMapper.findById(rs.getLong("challengee_deck"));
		
		return new Game(
				rs.getLong("id"),
				rs.getLong("version"),
				challenger, challengee,
				challengerDeck, challengeeDeck,
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
	
	public static GameBoard buildGameBoard(Game game) throws SQLException {
		
		long gameId = game.getId();
		long gameVersion = game.getVersion();
		int gameStatus = game.getStatus();
		
		User challenger = (User) game.getChallenger();
		User challengee = (User) game.getChallengee();
		Deck challengerDeck = (Deck) game.getChallengerDeck();
		Deck challengeeDeck = (Deck) game.getChallengeeDeck();
		
		List<ICardInPlay> challengerHand = CardInPlayMapper.findByGameAndPlayerAndStatus(gameId, challenger.getId(), CardStatus.hand.ordinal());
		List<ICardInPlay> challengeeHand = CardInPlayMapper.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.hand.ordinal());
		List<ICardInPlay> challengerBench = CardInPlayMapper.findByGameAndPlayerAndStatus(gameId, challenger.getId(), CardStatus.benched.ordinal());
		List<ICardInPlay> challengeeBench = CardInPlayMapper.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.benched.ordinal());
		List<ICardInPlay> challengerDiscarded = CardInPlayMapper.findByGameAndPlayerAndStatus(gameId, challenger.getId(), CardStatus.discarded.ordinal());
		List<ICardInPlay> challengeeDiscarded = CardInPlayMapper.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.discarded.ordinal());
		
		int challengerCardsNotInDeck = challengerHand.size() + challengerBench.size() + challengerDiscarded.size();
		int challengeeCardsNotInDeck = challengeeHand.size() + challengeeBench.size() + challengeeDiscarded.size();
		
		/**
		 * We make sure to remove X number of cards from the 40-card deck.
		 * Decks are always so it's just a matter of removing the first one X times.
		 * i.e. if we have 13 cards in play, we remove 13 cards from the top.
		 * So now we have 27 cards left.
		 */
		IntStream.range(0, challengerCardsNotInDeck).forEach($ -> challengerDeck.getCards().remove(0));
		IntStream.range(0, challengeeCardsNotInDeck).forEach($ -> challengeeDeck.getCards().remove(0));
		
		return new GameBoard(
				gameId, gameVersion,
				challenger, challengee,
				challengerDeck, challengeeDeck,
				challengerHand, challengeeHand,
				challengerBench, challengeeBench,
				challengerDiscarded, challengeeDiscarded,
				gameStatus
		);
		
	}

}
