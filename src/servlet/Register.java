package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/Register")
public class Register extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ALREADY_LOGGED_IN = "You are already logged in as %s.";
	private static final String ENTER_USER_AND_PASS = "Please enter both a username and a password.";
	private static final String USERNAME_TAKEN = "The username %s is taken.";
	
	private static final String REGISTRATION_SUCCESS = "Successfully registered.";

    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				doPost(request, response);
			}
			else {
				request.getRequestDispatcher(Global.REGISTER_FORM).forward(request, response);
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
			
			UserRDG userRDG;
			
			if (loggedIn(request)) {
				
				userRDG = UserRDG.findById(getUserId(request));
				UserHelper user = new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "");
				failure(request, response, String.format(ALREADY_LOGGED_IN, user.getUsername()));
				
			}
			else {
				
				String username = request.getParameter("user");
				String password = request.getParameter("pass");
				
				if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
					failure(request, response, ENTER_USER_AND_PASS);
				}
				else if ((userRDG = UserRDG.findByUsername(username)) != null) {
					failure(request, response, String.format(USERNAME_TAKEN, username));
				}
				else {
					userRDG = new UserRDG(UserRDG.getMaxId(), 1, username, password);
					userRDG.insert();
					request.getSession(true).setAttribute("userid", userRDG.getId());
					success(request, response, REGISTRATION_SUCCESS);
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
