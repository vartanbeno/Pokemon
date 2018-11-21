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
import app.dispatcher.AcceptChallengeDispatcher;
import app.dispatcher.ChallengePlayerDispatcher;
import app.dispatcher.DrawCardDispatcher;
import app.dispatcher.ListChallengesDispatcher;
import app.dispatcher.ListGamesDispatcher;
import app.dispatcher.ListPlayersDispatcher;
import app.dispatcher.LoginDispatcher;
import app.dispatcher.LogoutDispatcher;
import app.dispatcher.OpenChallengesDispatcher;
import app.dispatcher.PlayPokemonToBenchDispatcher;
import app.dispatcher.RefuseChallengeDispatcher;
import app.dispatcher.RegisterDispatcher;
import app.dispatcher.RetireDispatcher;
import app.dispatcher.UploadDeckDispatcher;
import app.dispatcher.ViewBoardDispatcher;
import app.dispatcher.ViewDeckDispatcher;
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
	
	public static final String REGISTER = "/Register";
	public static final String LOGIN = "/Login";
	public static final String LOGOUT = "/Logout";
	
	public static final String UPLOAD_DECK = "/UploadDeck";
	public static final String VIEW_DECK = "/ViewDeck";
	
	public static final String CHALLENGE_PLAYER = "/ChallengePlayer";
	public static final String OPEN_CHALLENGES = "/OpenChallenges";
	public static final String ACCEPT_CHALLENGE = "/AcceptChallenge";
	public static final String REFUSE_CHALLENGE = "/RefuseChallenge";
	
	public static final String LIST_PLAYERS = "/ListPlayers";
	public static final String LIST_CHALLENGES = "/ListChallenges";
	public static final String LIST_GAMES = "/ListGames";
	
	public static final String VIEW_BOARD = "/ViewBoard";
	public static final String VIEW_HAND = "/ViewHand";
	public static final String DRAW_CARD = "/DrawCard";
	public static final String PLAY_POKEMON_TO_BENCH = "/PlayPokemonToBench";
	public static final String RETIRE = "/Retire";
	
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
			request.setAttribute("message", "You probably want to go to an undefined endpoint.");
			request.getRequestDispatcher(Global.FAILURE).forward(request, response);
		}
		finally {
			super.postProcessRequest(request, response);
		}
		
	}
	
	private AbstractDispatcher getDispatcher(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
		
		AbstractDispatcher dispatcher = null;
		
		switch (path) {
		
		case REGISTER: dispatcher = new RegisterDispatcher(request, response);
			break;
		
		case LOGIN: dispatcher = new LoginDispatcher(request, response);
			break;
		
		case LOGOUT: dispatcher = new LogoutDispatcher(request, response);
			break;
		
		case UPLOAD_DECK: dispatcher = new UploadDeckDispatcher(request, response);
			break;
		
		case VIEW_DECK: dispatcher = new ViewDeckDispatcher(request, response);
			break;
		
		case CHALLENGE_PLAYER: dispatcher = new ChallengePlayerDispatcher(request, response);
			break;
		
		case OPEN_CHALLENGES: dispatcher = new OpenChallengesDispatcher(request, response);
			break;
		
		case ACCEPT_CHALLENGE: dispatcher = new AcceptChallengeDispatcher(request, response);
			break;
		
		case REFUSE_CHALLENGE: dispatcher = new RefuseChallengeDispatcher(request, response);
			break;
		
		case LIST_PLAYERS: dispatcher = new ListPlayersDispatcher(request, response);
			break;
		
		case LIST_CHALLENGES: dispatcher = new ListChallengesDispatcher(request, response);
			break;
		
		case LIST_GAMES: dispatcher = new ListGamesDispatcher(request, response);
			break;
		
		case VIEW_BOARD: dispatcher = new ViewBoardDispatcher(request, response);
			break;
		
		case VIEW_HAND: dispatcher = new ViewHandDispatcher(request, response);
			break;
		
		case DRAW_CARD: dispatcher = new DrawCardDispatcher(request, response);
			break;
		
		case PLAY_POKEMON_TO_BENCH: dispatcher = new PlayPokemonToBenchDispatcher(request, response);
			break;
		
		case RETIRE: dispatcher = new RetireDispatcher(request, response);
			break;
		
		default:
			break;
		
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
