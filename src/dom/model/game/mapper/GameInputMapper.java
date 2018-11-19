package dom.model.game.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayInputMapper;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameFactory;
import dom.model.game.IGame;
import dom.model.game.tdg.GameFinder;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class GameInputMapper {
	
	public static List<IGame> findAll() throws SQLException {
		
		ResultSet rs = GameFinder.findAll();
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static Game findById(long id) throws SQLException {
		
		Game game = getFromIdentityMap(id);
		if (game != null) return game;
		
		ResultSet rs = GameFinder.findById(id);
		
		game = rs.next() ? buildGame(rs) : null;
		rs.close();
		
		return game;
		
	}
	
	public static List<IGame> findByStatus(int status) throws SQLException {
		
		ResultSet rs = GameFinder.findByStatus(status);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallenger(long challenger) throws SQLException {
		
		ResultSet rs = GameFinder.findByChallenger(challenger);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallengee(long challengee) throws SQLException {
		
		ResultSet rs = GameFinder.findByChallengee(challengee);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallengerAndChallengee(long challenger, long challengee) throws SQLException {
		
		ResultSet rs = GameFinder.findByChallengerAndChallengee(challenger, challengee);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static List<IGame> findByChallengerOrChallengee(long player) throws SQLException {
		
		ResultSet rs = GameFinder.findByChallengerOrChallengee(player);
		
		List<IGame> games = buildGames(rs);
		rs.close();
		
		return games;
		
	}
	
	public static Game buildGame(ResultSet rs) throws SQLException {
		
		User challenger = UserInputMapper.findById(rs.getLong("challenger"));
		User challengee = UserInputMapper.findById(rs.getLong("challengee"));
		Deck challengerDeck = DeckInputMapper.findById(rs.getLong("challenger_deck"));
		Deck challengeeDeck = DeckInputMapper.findById(rs.getLong("challengee_deck"));
		
		return GameFactory.createClean(
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
			
			long id = rs.getLong("id");
			Game game = getFromIdentityMap(id);
			
			if (game == null) game = buildGame(rs);
			
			games.add(game);
			
		}
		
		rs.close();
		
		return games;
		
	}
	
	public static GameBoard buildGameBoard(Game game) throws SQLException {
		
		long gameId = game.getId();
		
		User challengerId = (User) game.getChallenger();
		User challengee = (User) game.getChallengee();
		Deck challengerDeck = (Deck) game.getChallengerDeck();
		Deck challengeeDeck = (Deck) game.getChallengeeDeck();
		
		List<ICardInPlay> challengerHand = CardInPlayInputMapper.findByGameAndPlayerAndStatus(gameId, challengerId.getId(), CardStatus.hand.ordinal());
		List<ICardInPlay> challengeeHand = CardInPlayInputMapper.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.hand.ordinal());
		List<ICardInPlay> challengerBench = CardInPlayInputMapper.findByGameAndPlayerAndStatus(gameId, challengerId.getId(), CardStatus.benched.ordinal());
		List<ICardInPlay> challengeeBench = CardInPlayInputMapper.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.benched.ordinal());
		List<ICardInPlay> challengerDiscarded = CardInPlayInputMapper.findByGameAndPlayerAndStatus(gameId, challengerId.getId(), CardStatus.discarded.ordinal());
		List<ICardInPlay> challengeeDiscarded = CardInPlayInputMapper.findByGameAndPlayerAndStatus(gameId, challengee.getId(), CardStatus.discarded.ordinal());
		
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
				game,
				challengerHand, challengeeHand,
				challengerBench, challengeeBench,
				challengerDiscarded, challengeeDiscarded
		);
		
	}
	
	public static Game getFromIdentityMap(long id) {
		
		Game game = null;
		
		try {
			game = IdentityMap.get(id, Game.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return game;
		
	}

}
