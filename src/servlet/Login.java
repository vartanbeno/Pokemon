package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.rdg.UserRDG;

@WebServlet("/Login")
public class Login extends PageController {
	
	private static final long serialVersionUID = 1L;
	
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(Global.LOGIN_FORM).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
						
			String username = request.getParameter("user");
			String password = request.getParameter("pass");
			
			UserRDG userRDG = null;
			
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
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

}
