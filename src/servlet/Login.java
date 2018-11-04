package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.user.rdg.UserRDG;

@WebServlet("/Login")
public class Login extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
		
	@Override
	public void init(javax.servlet.ServletConfig config) throws ServletException {
		try {
			String key = "";
			MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory(null, null, null, null);
			connectionFactory.defaultInitialization();
			DbRegistry.setConFactory(key, connectionFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher(Global.LOGIN_FORM).forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			String username = request.getParameter("user");
			String password = request.getParameter("pass");
			
			UserRDG userRDG = null;
			
			if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
				request.setAttribute("message", "Please enter both a username and a password.");
				request.getRequestDispatcher(Global.FAILURE).forward(request, response);
			}
			else if ((userRDG = UserRDG.findByUsernameAndPassword(username, password)) != null) {
				request.getSession(true).setAttribute("userid", userRDG.getId());
				request.setAttribute("message", "Successfully logged in.");
				request.getRequestDispatcher(Global.SUCCESS).forward(request, response);
			}
			else {
				request.setAttribute("message", "Incorrect username and/or password.");
				request.getRequestDispatcher(Global.FAILURE).forward(request, response);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
