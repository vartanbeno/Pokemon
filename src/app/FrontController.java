package app;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.dsrg.soenea.application.servlet.impl.SmartDispatcherServlet;
import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;
import org.dsrg.soenea.uow.MapperFactory;
import org.dsrg.soenea.uow.UoW;

import app.dispatcher.AbstractDispatcher;
import app.dispatcher.AcceptChallengeDispatcher;
import app.dispatcher.ChallengePlayerDispatcher;
import app.dispatcher.ChallengePlayerFormDispatcher;
import app.dispatcher.EndTurnDispatcher;
import app.dispatcher.ListChallengesDispatcher;
import app.dispatcher.ListGamesDispatcher;
import app.dispatcher.ListPlayersDispatcher;
import app.dispatcher.LoginDispatcher;
import app.dispatcher.LogoutDispatcher;
import app.dispatcher.OpenChallengesDispatcher;
import app.dispatcher.PlayCardDispatcher;
import app.dispatcher.RefuseChallengeDispatcher;
import app.dispatcher.RegisterDispatcher;
import app.dispatcher.RetireDispatcher;
import app.dispatcher.UploadDeckDispatcher;
import app.dispatcher.ViewBoardDispatcher;
import app.dispatcher.ViewDiscardPileDispatcher;
import app.dispatcher.DeckDispatcher;
import app.dispatcher.DecksDispatcher;
import app.dispatcher.ViewHandDispatcher;
import app.dispatcher.WithdrawFromChallengeDispatcher;
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
	
	public static final String CHALLENGE_PLAYER_FORM = BASE_URL + "/Player/Challenge";
	public static final String CHALLENGE_PLAYER = BASE_URL + "/Player/\\d+/Challenge";
	public static final String ACCEPT_CHALLENGE = BASE_URL + "/Challenge/\\d+/Accept";
	public static final String REFUSE_CHALLENGE = BASE_URL + "/Challenge/\\d+/Refuse";
	public static final String WITHDRAW_CHALLENGE = BASE_URL + "/Challenge/\\d+/Withdraw";
	public static final String OPEN_CHALLENGES = BASE_URL + "/Player/OpenChallenges";
	
	public static final String LIST_PLAYERS = BASE_URL + "/Player";
	public static final String LIST_CHALLENGES = BASE_URL + "/Challenge";
	public static final String LIST_GAMES = BASE_URL + "/Game";
	
	public static final String VIEW_BOARD = BASE_URL + "/Game/\\d+";
	public static final String VIEW_HAND = VIEW_BOARD + "/Hand";
	public static final String VIEW_DISCARD_PILE = VIEW_BOARD + "/Player/\\d+/Discard";
	public static final String PLAY_CARD = VIEW_BOARD + "/Hand/\\d+/Play";
	
	public static final String END_TURN = VIEW_BOARD + "/EndTurn";
	public static final String RETIRE = VIEW_BOARD + "/Retire";
	
	public static final String TRAILING_FORWARD_SLASHES = "\\/*";
	
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
			closeDb();
		}
		
	}
	
	/**
	 * Could not get the Permalink.xml file to work!
	 * TODO maybe a HashMap of different paths and a corresponding function.
	 */
	private AbstractDispatcher getDispatcher(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
		
		AbstractDispatcher dispatcher = null;
		
		if (path.equals(REGISTER)) {
			dispatcher = new RegisterDispatcher(request, response);
		}
		else if (isValid(path, LOGIN)) {
			dispatcher = new LoginDispatcher(request, response);
		}
		else if (isValid(path, LOGOUT)) {
			dispatcher = new LogoutDispatcher(request, response);
		}
		else if (isValid(path, UPLOAD_DECK_FORM)) {
			dispatcher = new UploadDeckDispatcher(request, response);
		}
		else if (isValid(path, VIEW_ALL_DECKS_OR_UPLOAD)) {
			dispatcher = new DecksDispatcher(request, response);
		}
		else if (isValid(path, VIEW_DECK)) {
			request.setAttribute("deck", getSplitPath(path)[2]);
			dispatcher = new DeckDispatcher(request, response);
		}
		else if (isValid(path, CHALLENGE_PLAYER_FORM)) {
			dispatcher = new ChallengePlayerFormDispatcher(request, response);
		}
		else if (isValid(path, CHALLENGE_PLAYER)) {
			request.setAttribute("challengee", getSplitPath(path)[2]);
			dispatcher = new ChallengePlayerDispatcher(request, response);
		}
		else if (isValid(path, ACCEPT_CHALLENGE)) {
			request.setAttribute("challenge", getSplitPath(path)[2]);
			dispatcher = new AcceptChallengeDispatcher(request, response);
		}
		else if (isValid(path, REFUSE_CHALLENGE)) {
			request.setAttribute("challenge", getSplitPath(path)[2]);
			dispatcher = new RefuseChallengeDispatcher(request, response);
		}
		else if (isValid(path, WITHDRAW_CHALLENGE)) {
			request.setAttribute("challenge", getSplitPath(path)[2]);
			dispatcher = new WithdrawFromChallengeDispatcher(request, response);
		}
		else if (isValid(path, OPEN_CHALLENGES)) {
			dispatcher = new OpenChallengesDispatcher(request, response);
		}
		else if (isValid(path, LIST_PLAYERS)) {
			dispatcher = new ListPlayersDispatcher(request, response);
		}
		else if (isValid(path, LIST_CHALLENGES)) {
			dispatcher = new ListChallengesDispatcher(request, response);
		}
		else if (isValid(path, LIST_GAMES)) {
			dispatcher = new ListGamesDispatcher(request, response);
		}
		else if (isValid(path, VIEW_BOARD)) {
			request.setAttribute("game", getSplitPath(path)[2]);
			dispatcher = new ViewBoardDispatcher(request, response);
		}
		else if (isValid(path, VIEW_HAND)) {
			request.setAttribute("game", getSplitPath(path)[2]);
			dispatcher = new ViewHandDispatcher(request, response);
		}
		else if (isValid(path, VIEW_DISCARD_PILE)) {
			request.setAttribute("game", getSplitPath(path)[2]);
			request.setAttribute("player", getSplitPath(path)[4]);
			dispatcher = new ViewDiscardPileDispatcher(request, response);
		}
		else if (isValid(path, PLAY_CARD)) {
			request.setAttribute("game", getSplitPath(path)[2]);
			request.setAttribute("card", getSplitPath(path)[4]);
			dispatcher = new PlayCardDispatcher(request, response);
		}
		else if (isValid(path, END_TURN)) {
			request.setAttribute("game", getSplitPath(path)[2]);
			dispatcher = new EndTurnDispatcher(request, response);
		}
		else if (isValid(path, RETIRE)) {
			request.setAttribute("game", getSplitPath(path)[2]);
			dispatcher = new RetireDispatcher(request, response);
		}
		
		return dispatcher;
		
	}
	
	private boolean isValid(String path, String pathRegex) {
		Pattern pattern = Pattern.compile(pathRegex + TRAILING_FORWARD_SLASHES);
		return pattern.matcher(path).matches();
	}
	
	/**
	 * E.g. /Poke/Deck/4
	 * Split it by "/", gives the array: [Poke, Deck, 4]
	 * We then use the resulting array to get the desired value.
	 * In this case, we would be interested in 4, the deck ID.
	 * 
	 * We use the StringUtils.split() method from org.apache.commons.lang, which removes empty results from the split.
	 * We set "/" as the delimiter.
	 * Link: https://stackoverflow.com/questions/9389503/how-to-prevent-java-lang-string-split-from-creating-a-leading-empty-string
	 */
	private String[] getSplitPath(String path) {
		return StringUtils.split(path, "/");
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
