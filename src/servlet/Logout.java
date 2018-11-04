package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.rdg.UserRDG;

@WebServlet("/Logout")
public class Logout extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    public Logout() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			long id = (long) request.getSession(true).getAttribute("userid");
			UserRDG userRDG = UserRDG.findById(id);
			
			request.getSession(true).invalidate();
			request.setAttribute("message", String.format("User %s has successfully logged out.", userRDG.getUsername()));
			request.getRequestDispatcher(Global.SUCCESS).forward(request, response);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
