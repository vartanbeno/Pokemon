package dom.model.game.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayMapper;
import dom.model.cardinplay.tdg.CardInPlayTDG;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.deck.tdg.DeckTDG;
import dom.model.game.Game;
import dom.model.game.GameBoard;
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
	
	public static GameBoard buildGameBoard(Game game) throws SQLException {
		
		long gameId = game.getId();
		int gameStatus = game.getStatus();
		
		User challenger = (User) game.getChallenger();
		User challengee = (User) game.getChallengee();
		Deck challengerDeck = (Deck) game.getChallengerDeck();
		Deck challengeeDeck = (Deck) game.getChallengeeDeck();
		
		ResultSet challengerHandRS = CardInPlayTDG.findByGameAndPlayerAndStatus(gameId, challenger.getId(), CardStatus.hand.ordinal());
		List<ICardInPlay> challengerHand = CardInPlayMapper.buildCardsInPlay(challengerHandRS);
		challengerHandRS.close();
		
		ResultSet challengeeHandRS = CardInPlayTDG.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.hand.ordinal());
		List<ICardInPlay> challengeeHand = CardInPlayMapper.buildCardsInPlay(challengeeHandRS);
		challengeeHandRS.close();
		
		ResultSet challengerBenchRS = CardInPlayTDG.findByGameAndPlayerAndStatus(gameId, challenger.getId(), CardStatus.benched.ordinal());
		List<ICardInPlay> challengerBench = CardInPlayMapper.buildCardsInPlay(challengerBenchRS);
		challengerBenchRS.close();
		
		ResultSet challengeeBenchRS = CardInPlayTDG.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.benched.ordinal());
		List<ICardInPlay> challengeeBench = CardInPlayMapper.buildCardsInPlay(challengeeBenchRS);
		challengeeBenchRS.close();
		
		ResultSet challengerDiscardedRS = CardInPlayTDG.findByGameAndPlayerAndStatus(gameId, challenger.getId(), CardStatus.discarded.ordinal());
		List<ICardInPlay> challengerDiscarded = CardInPlayMapper.buildCardsInPlay(challengerDiscardedRS);
		challengerDiscardedRS.close();
		
		ResultSet challengeeDiscardedRS = CardInPlayTDG.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.discarded.ordinal());
		List<ICardInPlay> challengeeDiscarded = CardInPlayMapper.buildCardsInPlay(challengeeDiscardedRS);
		challengeeDiscardedRS.close();
		
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
				gameId,
				challenger, challengee,
				challengerDeck, challengeeDeck,
				challengerHand, challengeeHand,
				challengerBench, challengeeBench,
				challengerDiscarded, challengeeDiscarded,
				gameStatus
		);
		
	}

}
