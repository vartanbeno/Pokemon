package dom.model.game;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.deck.IDeck;
import dom.model.game.tdg.GameTDG;
import dom.model.user.IUser;

public class GameFactory {
	
	public static Game createNew(
			IUser challenger, IUser challengee, IDeck challengerDeck, IDeck challengeeDeck, long currentTurn, long turn, int status
	) throws MissingMappingException, MapperException, SQLException {
		
		Game game = new Game(GameTDG.getMaxId(), 1, challenger, challengee, challengerDeck, challengeeDeck, currentTurn, turn, status);
		UoW.getCurrent().registerNew(game);
		
		return game;
		
	}
	
	public static Game createClean(
			long id, long version, IUser challenger, IUser challengee, IDeck challengerDeck, IDeck challengeeDeck, long currentTurn, long turn, int status
	) {
		
		Game game = new Game(id, version, challenger, challengee, challengerDeck, challengeeDeck, currentTurn, turn, status);
		UoW.getCurrent().registerClean(game);
		
		return game;
		
	}
	
	public static Game createClean(IGame game) {
		return createClean(
				game.getId(),
				game.getVersion(),
				game.getChallenger(),
				game.getChallengee(),
				game.getChallengerDeck(),
				game.getChallengeeDeck(),
				game.getCurrentTurn(),
				game.getTurn(),
				game.getStatus()
		);
	}
	
	public static Game registerDirty(
			long id, long version, IUser challenger, IUser challengee, IDeck challengerDeck, IDeck challengeeDeck, long currentTurn, long turn, int status
	) throws MissingMappingException, MapperException {
		
		Game game = new Game(id, version, challenger, challengee, challengerDeck, challengeeDeck, currentTurn, turn, status);
		UoW.getCurrent().registerDirty(game);
		
		return game;
		
	}
	
	public static Game registerDirty(IGame game)
			throws MissingMappingException, MapperException {
		return registerDirty(
				game.getId(),
				game.getVersion(),
				game.getChallenger(),
				game.getChallengee(),
				game.getChallengerDeck(),
				game.getChallengeeDeck(),
				game.getCurrentTurn(),
				game.getTurn(),
				game.getStatus()
		);
	}

}
