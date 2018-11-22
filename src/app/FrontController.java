package app;

import java.io.IOException;
import java.util.regex.Pattern;

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
import app.dispatcher.DeckDispatcher;
import app.dispatcher.DecksDispatcher;
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
	
	private static final String BASE_URL = "/Poke";
	
	public static final String REGISTER = BASE_URL + "/Player/Register";
	public static final String LOGIN = BASE_URL + "/Player/Login";
	public static final String LOGOUT = BASE_URL + "/Player/Logout";
	
	public static final String UPLOAD_DECK_FORM = BASE_URL + "/UploadDeck";
	public static final String VIEW_ALL_DECKS_OR_UPLOAD = BASE_URL + "/Deck";
	public static final String VIEW_DECK = VIEW_ALL_DECKS_OR_UPLOAD + "/\\d+";
	
	public static final String CHALLENGE_PLAYER = BASE_URL + "/Player/\\d+/Challenge";
	public static final String ACCEPT_CHALLENGE = CHALLENGE_PLAYER + "/Accept";
	public static final String REFUSE_CHALLENGE = CHALLENGE_PLAYER + "/Refuse";
	public static final String WITHDRAW_CHALLENGE = CHALLENGE_PLAYER + "/Withdraw";
	public static final String OPEN_CHALLENGES = BASE_URL + "/Player/Challenge";
	
	public static final String LIST_PLAYERS = BASE_URL + "/Player";
	public static final String LIST_CHALLENGES = BASE_URL + "/Challenge";
	public static final String LIST_GAMES = BASE_URL + "/Game";
	
	public static final String VIEW_BOARD = BASE_URL + "/Game/\\d+";
	public static final String VIEW_HAND = VIEW_BOARD + "/Hand";
	public static final String VIEW_DISCARD_PILE = VIEW_BOARD + "/Player/\\d+/Discard";
	public static final String DRAW_CARD = VIEW_BOARD + "/DrawCard";
	public static final String PLAY_POKEMON_TO_BENCH = VIEW_BOARD + "/Hand/\\d+/Play";
	public static final String END_TURN = VIEW_BOARD + "/EndTurn";
	public static final String RETIRE = VIEW_BOARD + "/Retire";
	
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
			
			/**
			 * We want to set the path as an attribute, so we can access the parameters that are in the URL itself.
			 * E.g. /Poke/Deck/4
			 * Split it by "/", gives the array: [Poke, Deck, 4]
			 * 
			 * We use the StringUtils.split(path, "/") method from org.apache.commons.lang, which removes empty results from the split.
			 * Link: https://stackoverflow.com/questions/9389503/how-to-prevent-java-lang-string-split-from-creating-a-leading-empty-string
			 */
			request.setAttribute("path", path);
			
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
			closeDb();
		}
		
	}
	
	/**
	 * Could not get the Permalink.xml file to work!
	 */
	private AbstractDispatcher getDispatcher(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
		
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
		else if (path.equals(UPLOAD_DECK_FORM)) {
			dispatcher = new UploadDeckDispatcher(request, response);
		}
		else if (path.equals(VIEW_ALL_DECKS_OR_UPLOAD)) {
			dispatcher = new DecksDispatcher(request, response);
		}
		else if (isValid(path, VIEW_DECK)) {
			dispatcher = new DeckDispatcher(request, response);
		}
		else if (isValid(path, CHALLENGE_PLAYER)) {
			dispatcher = new ChallengePlayerDispatcher(request, response);
		}
		else if (isValid(path, ACCEPT_CHALLENGE)) {
			dispatcher = new AcceptChallengeDispatcher(request, response);
		}
		else if (isValid(path, REFUSE_CHALLENGE)) {
			dispatcher = new RefuseChallengeDispatcher(request, response);
		}
		else if (isValid(path, WITHDRAW_CHALLENGE)) {
			// TODO WithdrawFromChallengeDispatcher
		}
		else if (path.equals(OPEN_CHALLENGES)) {
			dispatcher = new OpenChallengesDispatcher(request, response);
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
		else if (isValid(path, VIEW_BOARD)) {
			dispatcher = new ViewBoardDispatcher(request, response);
		}
		else if (isValid(path, VIEW_HAND)) {
			dispatcher = new ViewHandDispatcher(request, response);
		}
		else if (isValid(path, VIEW_DISCARD_PILE)) {
			// TODO ViewDiscardPileDispatcher
		}
		else if (isValid(path, DRAW_CARD)) {
			dispatcher = new DrawCardDispatcher(request, response);
		}
		else if (isValid(path, PLAY_POKEMON_TO_BENCH)) {
			dispatcher = new PlayPokemonToBenchDispatcher(request, response);
		}
		else if (isValid(path, END_TURN)) {
			// TODO EndTurnDispatcher
		}
		else if (isValid(path, RETIRE)) {
			dispatcher = new RetireDispatcher(request, response);
		}
		
		return dispatcher;
		
	}
	
	private boolean isValid(String path, String pathToMatch) {
		Pattern pattern = Pattern.compile(pathToMatch);
		return pattern.matcher(path).matches();
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
