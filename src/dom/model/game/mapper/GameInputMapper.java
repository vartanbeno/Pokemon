package dom.model.game.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.bench.IBench;
import dom.model.bench.mapper.BenchInputMapper;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.discard.IDiscard;
import dom.model.discard.mapper.DiscardInputMapper;
import dom.model.game.Game;
import dom.model.game.GameBoard;
import dom.model.game.GameFactory;
import dom.model.game.IGame;
import dom.model.game.tdg.GameFinder;
import dom.model.hand.IHand;
import dom.model.hand.mapper.HandInputMapper;
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
				rs.getLong("current_turn"),
				rs.getLong("turn"),
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
		
		List<IHand> challengerHand = HandInputMapper.findByGameAndPlayer(gameId, challengerId.getId());
		List<IHand> challengeeHand = HandInputMapper.findByGameAndPlayer(gameId, challengee.getId());
		List<IBench> challengerBench = BenchInputMapper.findByGameAndPlayer(gameId, challengerId.getId());
		List<IBench> challengeeBench = BenchInputMapper.findByGameAndPlayer(gameId, challengee.getId());
		List<IDiscard> challengerDiscarded = DiscardInputMapper.findByGameAndPlayer(gameId, challengerId.getId());
		List<IDiscard> challengeeDiscarded = DiscardInputMapper.findByGameAndPlayer(gameId, challengee.getId());
		
		/**
		 * We also have to take into account energy cards that are attached to benched Pokemon.
		 */
		int challengerAttachedEnergies = 0;		
		for (IBench benchCard : challengerBench) {
			challengerAttachedEnergies += benchCard.getAttachedEnergyCards().size();
		}
		
		int challengeeAttachedEnergies = 0;
		for (IBench benchCard : challengeeBench) {
			challengeeAttachedEnergies += benchCard.getAttachedEnergyCards().size();
		}
		
		int challengerCardsNotInDeck = challengerHand.size() + challengerBench.size() + challengerDiscarded.size() + challengerAttachedEnergies;
		int challengeeCardsNotInDeck = challengeeHand.size() + challengeeBench.size() + challengeeDiscarded.size() + challengeeAttachedEnergies;
		
		/**
		 * We make sure to remove X number of cards from the 40-card deck.
		 * Decks are always ordered so it's just a matter of removing the first one X times.
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
