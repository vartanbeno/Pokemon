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
	
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Long userId = null;
		
		try {
			
			userId = (long) request.getSession(true).getAttribute("userid");
			
			UserRDG userRDG = null;

			try {
				userRDG = UserRDG.findById(userId);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			UserHelper user = new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "");
			failure(request, response, String.format("You are already logged in as %s.", user.getUsername()));
		
		}
		catch (NullPointerException e) {
			request.getRequestDispatcher(Global.LOGIN_FORM).forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Long userId = null;
			UserRDG userRDG = null;
			
			try {
				
				userId = (long) request.getSession(true).getAttribute("userid");
				
				userRDG = UserRDG.findById(userId);
				UserHelper user = new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "");
				
				failure(request, response, String.format("You are already logged in as %s.", user.getUsername()));
				
			}
			catch (NullPointerException e) {
				
				String username = request.getParameter("user");
				String password = request.getParameter("pass");
				
				if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
					failure(request, response, "Please enter both a username and a password.");
				}
				else if ((userRDG = UserRDG.findByUsernameAndPassword(username, password)) != null) {
					request.getSession(true).setAttribute("userid", userRDG.getId());
					success(request, response, "Successfully logged in.");
				}
				else {
					failure(request, response, "Incorrect username and/or password.");
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
