package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.User;
import dom.model.user.mapper.UserMapper;

@WebServlet("/Logout")
public class Logout extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOGOUT_FAIL = "You must be logged in to log out.";
	
	/**
	 * While testing, I registered/logged in as a user at some point.
	 * Pretty normal, right?
	 * Before logging out, I manually deleted the user I was currently logged in as from the database.
	 * I couldn't visit any of the pages anymore, much less log out.
	 * Gotta do some more error handling.
	 */
	private static final String MAJOR_FAIL = "Something went horribly wrong. We can't point you to a user in the database.";
	
	private static final String LOGOUT_SUCCESS = "User %s has successfully logged out.";
	
    public Logout() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, LOGOUT_FAIL);
				return;
			}
				
			User user = UserMapper.findById(getUserId(request));
			
			try {
				request.getSession(true).invalidate();
				success(request, response, String.format(LOGOUT_SUCCESS, user.getUsername()));
			}
			catch (NullPointerException e) {
				request.getSession(true).invalidate();
				success(request, response, MAJOR_FAIL);
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
