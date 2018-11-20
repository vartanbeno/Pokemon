package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.application.servlet.impl.SmartDispatcherServlet;
import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;
import org.dsrg.soenea.uow.MapperFactory;
import org.dsrg.soenea.uow.UoW;

import app.dispatcher.RegisterDispatcher;
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
		
		String path = request.getServletPath();
		
		System.out.println("=============================================");
		System.out.println(path);
		System.out.println("=============================================");
		
		try {
			Dispatcher dispatcher = getDispatcher(request, response, path);
			dispatcher.init(request, response);
			dispatcher.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			super.postProcessRequest(request, response);
		}
		
	}
	
	private Dispatcher getDispatcher(HttpServletRequest request, HttpServletResponse response, String path) {
		
		Dispatcher dispatcher = null;
		
		if (path.equals("/Register")) {
			dispatcher = new RegisterDispatcher(request, response);
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
