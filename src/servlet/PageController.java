package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.dsrg.soenea.service.threadLocal.ThreadLocalTracker;

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

    public PageController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	initDb();
    }
    
    public static synchronized void initDb() {
    	    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			String key = "";
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
	
}
