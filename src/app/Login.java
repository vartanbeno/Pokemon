package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.rdg.UserRDG;

@WebServlet("/Login")
public class Login extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ENTER_USER_AND_PASS = "Please enter both a username and a password.";
	private static final String INVALID_CREDENTIALS = "Incorrect username and/or password.";
	
	private static final String LOGIN_SUCCESS = "Successfully logged in as %s.";
	
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(Global.LOGIN_FORM).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			UserRDG userRDG = null;
				
			String username = request.getParameter("user");
			String password = request.getParameter("pass");
			
			if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
				failure(request, response, ENTER_USER_AND_PASS);
			}
			else if ((userRDG = UserRDG.findByUsernameAndPassword(username, password)) != null) {
				request.getSession(true).setAttribute("userid", userRDG.getId());
				success(request, response, String.format(LOGIN_SUCCESS, username));
			}
			else {
				failure(request, response, INVALID_CREDENTIALS);
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
