package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.application.servlet.impl.SmartDispatcherServlet;
import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;
import org.dsrg.soenea.uow.MapperFactory;
import org.dsrg.soenea.uow.UoW;

import app.dispatcher.AbstractDispatcher;
import app.dispatcher.ChallengePlayerDispatcher;
import app.dispatcher.ListChallengesDispatcher;
import app.dispatcher.ListGamesDispatcher;
import app.dispatcher.ListPlayersDispatcher;
import app.dispatcher.LoginDispatcher;
import app.dispatcher.LogoutDispatcher;
import app.dispatcher.OpenChallengesDispatcher;
import app.dispatcher.RegisterDispatcher;
import app.dispatcher.UploadDeckDispatcher;
import app.dispatcher.ViewBoardDispatcher;
import app.dispatcher.ViewHandDispatcher;
import dom.model.card.Card;
import dom.model.card.mapper.CardOutputMapper;
import dom.model.card.tdg.CardTDG;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.mapper.CardInPlayOutputMapper;
import dom.model.cardinplay.tdg.CardInPlayTDG;
import dom.model.challenge.Challenge;
import dom.model.challenge.mapper.ChallengeOutputMapper;
import dom.model.challenge.tdg.ChallengeTDG;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckOutputMapper;
import dom.model.deck.tdg.DeckTDG;
import dom.model.game.Game;
import dom.model.game.mapper.GameOutputMapper;
import dom.model.game.tdg.GameTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserOutputMapper;
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
@WebServlet("/")
public class FrontController extends SmartDispatcherServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String GET = "GET";
	private static final String POST = "POST";
	
	private static final String REGISTER = "/Register";
	private static final String LOGIN = "/Login";
	private static final String LOGOUT = "/Logout";
	
	private static final String UPLOAD_DECK = "/UploadDeck";
	private static final String VIEW_DECK = "/ViewDeck";
	
	private static final String CHALLENGE_PLAYER = "/ChallengePlayer";
	private static final String OPEN_CHALLENGES = "/OpenChallenges";
	private static final String ACCEPT_CHALLENGE = "/AcceptChallenge";
	private static final String REFUSE_CHALLENGE = "/RefuseChallenge";
	
	private static final String LIST_PLAYERS = "/ListPlayers";
	private static final String LIST_CHALLENGES = "/ListChallenges";
	private static final String LIST_GAMES = "/ListGames";
	
	private static final String VIEW_BOARD = "/ViewBoard";
	private static final String VIEW_HAND = "/ViewHand";
	private static final String DRAW_CARD = "/DrawCard";
	private static final String PLAY_POKEMON_TO_BENCH = "/PlayPokemonToBench";
	private static final String RETIRE = "/Retire";
	
    public FrontController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	initDb("");
    	initUoW();
    }
    
    public static synchronized void initDb(String key) {
    	    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory(null, null, null, null);
			connectionFactory.defaultInitialization();
			DbRegistry.setConFactory(key, connectionFactory);
			DbRegistry.setTablePrefix(key, "");
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
			UserTDG.dropTable();
			DeckTDG.dropTable();
			ChallengeTDG.dropTable();
			CardTDG.dropTable();
			GameTDG.dropTable();
			CardInPlayTDG.dropTable();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public static void initUoW() {
    	
    	MapperFactory mapperFactory = new MapperFactory();
    	
    	mapperFactory.addMapping(User.class, UserOutputMapper.class);
    	mapperFactory.addMapping(Deck.class, DeckOutputMapper.class);
    	mapperFactory.addMapping(Challenge.class, ChallengeOutputMapper.class);
    	mapperFactory.addMapping(Card.class, CardOutputMapper.class);
    	mapperFactory.addMapping(Game.class, GameOutputMapper.class);
    	mapperFactory.addMapping(CardInPlay.class, CardInPlayOutputMapper.class);
    	
    	UoW.initMapperFactory(mapperFactory);
    	
    }
	
	@Override
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		super.preProcessRequest(request, response);
		
		try {
			
			String path = request.getServletPath();
			
			AbstractDispatcher dispatcher = (AbstractDispatcher) getDispatcher(request, response, path);
			dispatcher.init(request, response);
			
			if (request.getMethod().equals(GET)) dispatcher.doGet();
			if (request.getMethod().equals(POST)) dispatcher.execute();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			super.postProcessRequest(request, response);
		}
		
	}
	
	private AbstractDispatcher getDispatcher(HttpServletRequest request, HttpServletResponse response, String path) {
		
		AbstractDispatcher dispatcher = null;
		
		if (path.equals(REGISTER)) {
			dispatcher = new RegisterDispatcher(request, response);
		}
		else if (path.equals(LOGIN)) {
			dispatcher = new LoginDispatcher(request, response);
		}
		else if (path.equals(LOGOUT)) {
			dispatcher = new LogoutDispatcher(request, response);
		}
		else if (path.equals(LIST_PLAYERS)) {
			dispatcher = new ListPlayersDispatcher(request, response);
		}
		else if (path.equals(LIST_CHALLENGES)) {
			dispatcher = new ListChallengesDispatcher(request, response);
		}
		else if (path.equals(LIST_GAMES)) {
			dispatcher = new ListGamesDispatcher(request, response);
		}
		else if (path.equals(OPEN_CHALLENGES)) {
			dispatcher = new OpenChallengesDispatcher(request, response);
		}
		else if (path.equals(CHALLENGE_PLAYER)) {
			dispatcher = new ChallengePlayerDispatcher(request, response);
		}
		else if (path.equals(VIEW_BOARD)) {
			dispatcher = new ViewBoardDispatcher(request, response);
		}
		else if (path.equals(VIEW_HAND)) {
			dispatcher = new ViewHandDispatcher(request, response);
		}
		else if (path.equals(UPLOAD_DECK)) {
			dispatcher = new UploadDeckDispatcher(request, response);
		}
		
		return dispatcher;
		
	}

	@Override
	protected String getXMLErrorTemplate() {
		return Global.FAILURE;
	}

	@Override
	protected String getJSONErrorTemplate() {
		return Global.FAILURE;
	}

	@Override
	protected String getErrorTemplate() {
		return Global.FAILURE;
	}

	@Override
	protected String getMessageTemplate() {
		return Global.FAILURE;
	}

	@Override
	protected String getMainTemplate() {
		return Global.FAILURE;
	}
	
}
