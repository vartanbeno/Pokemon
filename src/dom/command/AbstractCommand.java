package dom.command;

import java.sql.SQLException;
import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.challenge.Challenge;
import dom.model.challenge.mapper.ChallengeInputMapper;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.game.Game;
import dom.model.game.mapper.GameInputMapper;

public abstract class AbstractCommand extends ValidatorCommand {
	
	@SetInRequestAttribute
	public String message;
	
	/**
	 * Accept challenge fail messages.
	 */
	private static final String CHALLENGE_ID_FORMAT = "You must specify a challenge ID in the correct format (a positive integer).";
	private static final String ACCEPT_CHALLENGE_SAME_ID = "You cannot accept a challenge against yourself.";
	private static final String ACCEPT_CHALLENGE_DOES_NOT_EXIST = "You cannot accept a challenge that doesn't exist.";
	
	/**
	 * Refuse challenge fail messages.
	 */
	private static final String REFUSE_CHALLENGE_DOES_NOT_EXIST = "You cannot refuse/withdraw from a challenge that doesn't exist.";
	
	/**
	 * Accept/refuse/withdraw challenge fail messages.
	 */
	private static final String NOT_PART_OF_CHALLENGE = "You must be part of the challenge to accept/refuse/withdraw from it.";
	
	/**
	 * Deck fail messages.
	 */
	private static final String DECK_ID_FORMAT = "You must specify a deck ID in the correct format (a positive integer).";
	private static final String DECK_DOES_NOT_EXIST = "The deck you specified does not exist.";
	
	/**
	 * Game fail messages.
	 */
	private static final String GAME_ID_FORMAT = "You must specify a game ID in the correct format (a positive integer).";
	private static final String GAME_DOES_NOT_EXIST = "The game you specified does not exist.";
	private static final String NOT_YOUR_GAME = "You are not part of this game.";

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
	
	protected List<IDeck> getMyDecks() throws SQLException, CommandException {
		
		long userId = getUserId();
		
		try {
			return DeckInputMapper.findByPlayer(userId);
		}
		catch (SQLException e) {
			throw new CommandException(e.getMessage());
		}
		
	}
	
	protected Challenge getChallengeToAccept() throws SQLException, CommandException {
		
		Long challengeId = null;
		long userId = getUserId();
		
		try {
			challengeId = helper.getLong("challenge");
		}
		catch (NumberFormatException e) {
			throw new CommandException(CHALLENGE_ID_FORMAT);
		}
		
		Challenge challenge = ChallengeInputMapper.findById(challengeId);
		
		if (challenge == null) throw new CommandException(ACCEPT_CHALLENGE_DOES_NOT_EXIST);
		if (userId == challenge.getChallenger().getId()) throw new CommandException(ACCEPT_CHALLENGE_SAME_ID);
		if (userId != challenge.getChallenger().getId() && userId != challenge.getChallengee().getId())
			throw new CommandException(NOT_PART_OF_CHALLENGE);
		
		return challenge;
		
	}
	
	protected Challenge getChallengeToRefuseOrWithdraw() throws SQLException, CommandException {
		
		Long challengeId = null;
		long userId = getUserId();
		
		try {
			challengeId = helper.getLong("challenge");
		}
		catch (NumberFormatException e) {
			throw new CommandException(CHALLENGE_ID_FORMAT);
		}
		
		Challenge challenge = ChallengeInputMapper.findById(challengeId);
		
		if (challenge == null) throw new CommandException(REFUSE_CHALLENGE_DOES_NOT_EXIST);
		if (userId != challenge.getChallenger().getId() && userId != challenge.getChallengee().getId())
			throw new CommandException(NOT_PART_OF_CHALLENGE);
		
		return challenge;
		
	}
	
	protected Game getGame() throws CommandException, SQLException {
		
		Long gameId = null;
		long userId = getUserId();
		
		try {
			gameId = helper.getLong("game");
		}
		catch (NumberFormatException e) {
			throw new CommandException(GAME_ID_FORMAT);
		}
		
		Game game = GameInputMapper.findById(gameId);
		if (game == null) throw new CommandException(GAME_DOES_NOT_EXIST);
		if (game.getChallenger().getId() != userId && game.getChallengee().getId() != userId) throw new CommandException(NOT_YOUR_GAME);
		
		return game;
		
	}

}
