package dom.model.game;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class GameFactory {
	
	public static Game createNew(long id, long version, IUser challenger, IUser challengee, IDeck challengerDeck, IDeck challengeeDeck, int status)
			throws MissingMappingException, MapperException {
		
		Game game = new Game(id, version, challenger, challengee, challengerDeck, challengeeDeck, status);
		UoW.getCurrent().registerNew(game);
		
		return game;
		
	}
	
	public static Game createNew(IGame game)
			throws MissingMappingException, MapperException {
		return createNew(
				game.getId(),
				game.getVersion(),
				game.getChallenger(),
				game.getChallengee(),
				game.getChallengerDeck(),
				game.getChallengeeDeck(),
				game.getStatus()
		);
	}
	
	public static Game createClean(long id, long version, IUser challenger, IUser challengee, IDeck challengerDeck, IDeck challengeeDeck, int status) {
		
		Game game = new Game(id, version, challenger, challengee, challengerDeck, challengeeDeck, status);
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
				game.getStatus()
		);
	}

}
