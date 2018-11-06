package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/Login")
public class Login extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ALREADY_LOGGED_IN = "You are already logged in as %s.";
	private static final String ENTER_USER_AND_PASS = "Please enter both a username and a password.";
	private static final String INVALID_CREDENTIALS = "Incorrect username and/or password.";
	
	private static final String LOGIN_SUCCESS = "Successfully logged in.";
	
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
								
				UserRDG userRDG = UserRDG.findById(getUserId(request));
				UserHelper user = new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "");
				failure(request, response, String.format(ALREADY_LOGGED_IN, user.getUsername()));

			}
			else {
				request.getRequestDispatcher(Global.LOGIN_FORM).forward(request, response);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				doGet(request, response);
			}
			else {
				
				UserRDG userRDG = null;
				
				String username = request.getParameter("user");
				String password = request.getParameter("pass");
				
				if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
					failure(request, response, ENTER_USER_AND_PASS);
				}
				else if ((userRDG = UserRDG.findByUsernameAndPassword(username, password)) != null) {
					request.getSession(true).setAttribute("userid", userRDG.getId());
					success(request, response, LOGIN_SUCCESS);
				}
				else {
					failure(request, response, INVALID_CREDENTIALS);
				}
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

}
