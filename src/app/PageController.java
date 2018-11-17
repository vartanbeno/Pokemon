package app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;

import dom.model.card.tdg.CardTDG;
import dom.model.cardinplay.tdg.CardInPlayTDG;
import dom.model.challenge.Challenge;
import dom.model.challenge.mapper.ChallengeMapper;
import dom.model.challenge.tdg.ChallengeTDG;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.deck.tdg.DeckTDG;
import dom.model.game.Game;
import dom.model.game.mapper.GameMapper;
import dom.model.game.tdg.GameTDG;
import dom.model.user.tdg.UserTDG;

/**
 * 
 * Some of this code, like the initDb() and closeDb() methods, are taken from
 * Stuart Thiel's thesis, Enterprise Application Design Patterns: Improved and Applied.
 * Using the DbRegistry class from his SoenEA2 framework to establish a database connection.
 * 
 * @author vartanbeno
 *
 */
@WebServlet("/PageController")
public class PageController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Accept challenge fail messages.
	 */
	private static final String WRONG_CHALLENGE_ID_FORMAT = "You must specify a challenge ID in the correct format (a positive integer).";
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
	private static final String WRONG_DECK_ID_FORMAT = "You must specify a deck ID in the correct format (a positive integer).";
	private static final String DECK_DOES_NOT_EXIST = "The deck you specified does not exist.";
	
	/**
	 * Game fail messages.
	 */
	private static final String WRONG_GAME_ID_FORMAT = "You must specify a game ID in the correct format (a positive integer).";
	private static final String GAME_DOES_NOT_EXIST = "The game you specified does not exist.";
	private static final String NOT_YOUR_GAME = "You are not part of this game.";

    public PageController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	initDb("");
    }
    
    public static synchronized void initDb(String key) {
    	    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory(null, null, null, null);
			connectionFactory.defaultInitialization();
			DbRegistry.setConFactory(key, connectionFactory);
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    public static void closeDb() {
    	try {
    		DbRegistry.closeDbConnectionIfNeeded();
    		ThreadLocalTracker.purgeThreadLocal();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void createTables() {
    	try {
    		UserTDG.createTable();
			DeckTDG.createTable();
			ChallengeTDG.createTable();
			CardTDG.createTable();
			GameTDG.createTable();
			CardInPlayTDG.createTable();
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void dropTables() {
    	try {
    		CardInPlayTDG.dropTable();
			GameTDG.dropTable();
			CardTDG.dropTable();
			ChallengeTDG.dropTable();
			DeckTDG.dropTable();
			UserTDG.dropTable();
    	}
    	catch (Exception e) { }
    }

	protected void success(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("message", message);
		request.getRequestDispatcher(Global.SUCCESS).forward(request, response);
	}
	
	protected void failure(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
		request.setAttribute("message", message);
		request.getRequestDispatcher(Global.FAILURE).forward(request, response);
	}
	
	protected long getUserId(HttpServletRequest request) {
		return (long) request.getSession(true).getAttribute("userid");
	}
	
	protected boolean loggedIn(HttpServletRequest request) {
		
		Long userId;
		try {
			userId = getUserId(request);
		}
		catch (NullPointerException e) {
			userId = null;
		}
		
		return userId != null;
		
	}
	
	protected Challenge getChallengeToAccept(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			long challengeId = Long.parseLong(request.getParameter("challenge"));
			long userId = getUserId(request);
			
			Challenge challenge = ChallengeMapper.findById(challengeId);
			
			if (challenge == null) {
				failure(request, response, ACCEPT_CHALLENGE_DOES_NOT_EXIST);
			}
			else if (userId == challenge.getChallenger().getId()) {
				challenge = null;
				failure(request, response, ACCEPT_CHALLENGE_SAME_ID);
			}
			else if (userId != challenge.getChallenger().getId() && userId != challenge.getChallengee().getId()) {
				challenge = null;
				failure(request, response, NOT_PART_OF_CHALLENGE);
			}
			
			return challenge;
			
		}
		catch (NumberFormatException e) {
			failure(request, response, WRONG_CHALLENGE_ID_FORMAT);
			return null;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	protected Challenge getChallengeToRefuseOrWithdraw(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			long challengeId = Long.parseLong(request.getParameter("challenge"));
			long userId = getUserId(request);
			
			Challenge challenge = ChallengeMapper.findById(challengeId);
			
			if (challenge == null) {
				failure(request, response, REFUSE_CHALLENGE_DOES_NOT_EXIST);
			}
			else if (userId != challenge.getChallenger().getId() && userId != challenge.getChallengee().getId()) {
				challenge = null;
				failure(request, response, NOT_PART_OF_CHALLENGE);
			}
			
			return challenge;
			
		}
		catch (NumberFormatException e) {
			failure(request, response, WRONG_CHALLENGE_ID_FORMAT);
			return null;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	protected Deck getDeck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			long deckId = Long.parseLong(request.getParameter("deck"));
			
			Deck deck = DeckMapper.findById(deckId);
			
			if (deck == null) {
				failure(request, response, DECK_DOES_NOT_EXIST);
			}
			
			return deck;
			
		}
		catch (NumberFormatException e) {
			failure(request, response, WRONG_DECK_ID_FORMAT);
			return null;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	protected List<IDeck> getMyDecks(HttpServletRequest request) throws ServletException, IOException {
		
		List<IDeck> decks = new ArrayList<IDeck>();
		try {
			decks = DeckMapper.findByPlayer(getUserId(request));
			return decks;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return decks;
		}
		
	}
	
	protected Game getGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			long gameId = Long.parseLong(request.getParameter("game"));
			long userId = getUserId(request);
			
			Game game = GameMapper.findById(gameId);
			
			if (game == null) {
				failure(request, response, GAME_DOES_NOT_EXIST);
			}
			else if (userId != game.getChallenger().getId() && userId != game.getChallengee().getId()) {
				game = null;
				failure(request, response, NOT_YOUR_GAME);
			}
			
			return game;
			
		}
		catch (NumberFormatException e) {
			failure(request, response, WRONG_GAME_ID_FORMAT);
			return null;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
