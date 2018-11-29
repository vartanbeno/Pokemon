package dom.command;

import java.sql.SQLException;
import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.mapper.ChallengeInputMapper;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.game.Game;
import dom.model.game.GameStatus;
import dom.model.game.IGame;
import dom.model.game.mapper.GameInputMapper;

public abstract class AbstractCommand extends ValidatorCommand {
	
	@SetInRequestAttribute
	public String message;
	
	/**
	 * Version fail message.
	 * Message will be shown if either:
	 *  - version isn't provided as a parameter;
	 *  - version provided can't be parsed as a long.
	 */
	private static final String VERSION_FAIL = "You must provide a version number.";
	
	/**
	 * Challenge fail messages.
	 */
	private static final String CHALLENGE_NOT_OPEN = "You cannot accept/refuse/withdraw from a challenge that's not open.";
	private static final String CHALLENGE_DOES_NOT_EXIST = "You cannot accept/refuse/withdraw from a challenge that doesn't exist.";
	private static final String NOT_PART_OF_CHALLENGE = "You must be part of the challenge to accept/refuse/withdraw from it.";
	private static final String SAME_ID = "You cannot accept/refuse/withdraw from a challenge against yourself.";
	
	/**
	 * Deck fail messages.
	 */
	private static final String NOT_YOUR_DECK = "The deck you specified is not yours.";
	private static final String DECK_ID_FORMAT = "You must specify a deck ID in the correct format (a positive integer).";
	private static final String DECK_DOES_NOT_EXIST = "The deck you specified does not exist.";
	
	/**
	 * Game fail messages.
	 */
	private static final String GAME_DOES_NOT_EXIST = "The game you specified does not exist.";
	private static final String NOT_YOUR_GAME = "You are not part of this game.";
	private static final String GAME_HAS_ENDED = "This game has already ended.";
	private static final String NOT_YOUR_TURN = "It is not your turn yet.";

	public AbstractCommand(Helper helper) {
		super(helper);
	}

	@Override
	public abstract void process() throws CommandException;
	
	protected long getUserId() {
		return (long) helper.getSessionAttribute("userid");
	}
	
	protected void checkIfLoggedIn(String message) throws CommandException {
		try {
			getUserId();
		}
		catch (NullPointerException e) {
			throw new CommandException(message);
		}
	}
	
	protected long getVersion() throws CommandException {
		try {
			return helper.getLong("version");
		}
		catch (NumberFormatException e) {
			throw new CommandException(VERSION_FAIL);
		}
	}
	
	protected Deck getDeck() throws SQLException, CommandException {
		
		Long deckId = null;
		try {
			deckId = helper.getLong("deck");
		}
		catch (NumberFormatException e) {
			throw new CommandException(DECK_ID_FORMAT);
		}
		
		Deck deck = DeckInputMapper.findById(deckId);
		if (deck == null) throw new CommandException(DECK_DOES_NOT_EXIST);
		
		return deck;
		
	}
	
	protected Deck getDeck(long deckId) throws SQLException, CommandException {
		
		Deck deck = DeckInputMapper.findById(deckId);
		if (deck == null) throw new CommandException(DECK_DOES_NOT_EXIST);
		
		return deck;
		
	}
	
	protected void checkIfItsMyDeck(IDeck deck) throws CommandException {
		if (getUserId() != deck.getPlayer().getId()) throw new CommandException(NOT_YOUR_DECK);
	}
	
	protected List<IDeck> getMyDecks() throws SQLException, CommandException {
		
		long userId = getUserId();
		
		try {
			return DeckInputMapper.findByPlayer(userId);
		}
		catch (SQLException e) {
			throw new CommandException(e.getMessage());
		}
		
	}
	
	protected Challenge getChallengeToAcceptOrRefuse(long challengeId) throws SQLException, CommandException {
		
		long userId = getUserId();
		Challenge challenge = ChallengeInputMapper.findById(challengeId);
		
		validateChallenge(challenge);
		
		if (userId == challenge.getChallenger().getId()) throw new CommandException(SAME_ID);
		
		return challenge;
		
	}
	
	protected Challenge getChallengeToWithdrawFrom(long challengeId) throws SQLException, CommandException {
		
		long userId = getUserId();
		Challenge challenge = ChallengeInputMapper.findById(challengeId);
		
		validateChallenge(challenge);
		
		if (userId == challenge.getChallengee().getId()) throw new CommandException(SAME_ID);
		
		return challenge;
		
	}
	
	protected void validateChallenge(Challenge challenge) throws CommandException {
		
		long userId = getUserId();
		
		if (challenge == null) throw new CommandException(CHALLENGE_DOES_NOT_EXIST);
		
		if (userId != challenge.getChallenger().getId() && userId != challenge.getChallengee().getId())
			throw new CommandException(NOT_PART_OF_CHALLENGE);
		
		if (challenge.getStatus() != ChallengeStatus.open.ordinal())
			throw new CommandException(CHALLENGE_NOT_OPEN);
						
	}
	
	protected Game getGame(long gameId) throws CommandException, SQLException {
		Game game = GameInputMapper.findById(gameId);
		checkIfGameExists(game);
		return game;
	}
	
	protected void checkIfGameExists(Game game) throws CommandException {
		if (game == null) throw new CommandException(GAME_DOES_NOT_EXIST);
	}
	
	protected void checkIfImPartOfGame(IGame game) throws CommandException {
		long userId = getUserId();
		if (userId != game.getChallenger().getId() && userId != game.getChallengee().getId())
			throw new CommandException(NOT_YOUR_GAME);
	}
	
	protected void checkIfGameHasEnded(IGame game) throws CommandException {
		if (game.getStatus() != GameStatus.ongoing.ordinal()) throw new CommandException(GAME_HAS_ENDED);
	}
	
	protected void checkIfItsMyTurn(IGame game) throws CommandException {
		if (getUserId() != game.getCurrentTurn()) throw new CommandException(NOT_YOUR_TURN);
	}

}
