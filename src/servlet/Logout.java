package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.rdg.UserRDG;

@WebServlet("/Logout")
public class Logout extends PageController {
	
	private static final long serialVersionUID = 1L;
	
    public Logout() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				long id = getUserId(request);
				
				UserRDG userRDG = UserRDG.findById(id);
				
				request.getSession(true).invalidate();
				success(request, response, String.format("User %s has successfully logged out.", userRDG.getUsername()));
			}
			else {
				failure(request, response, "You must be logged in to log out.");
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
