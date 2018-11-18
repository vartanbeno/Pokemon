package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.User;
import dom.model.user.mapper.UserMapper;
import dom.model.user.tdg.UserTDG;

@WebServlet("/Register")
public class Register extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ENTER_USER_AND_PASS = "Please enter both a username and a password.";
	private static final String USERNAME_TAKEN = "The username %s is taken.";
	
	private static final String REGISTRATION_SUCCESS = "Successfully registered as %s.";

    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(Global.REGISTER_FORM).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
						
			String username = request.getParameter("user");
			String password = request.getParameter("pass");
			
			User user = UserMapper.findByUsername(username);
			
			if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
				failure(request, response, ENTER_USER_AND_PASS);
			}
			else if (user != null) {
				failure(request, response, String.format(USERNAME_TAKEN, username));
			}
			else {
				user = new User(UserTDG.getMaxId(), 1, username, password);
				UserMapper.insertStatic(user);
				request.getSession(true).setAttribute("userid", user.getId());
				success(request, response, String.format(REGISTRATION_SUCCESS, user.getUsername()));
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
