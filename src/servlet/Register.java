package servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Register")
public class Register extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SUCCESS_JSP = "WEB-INF/jsp/success.jsp";
	private static final String FAILURE_JSP = "WEB-INF/jsp/fail.jsp";
	
	public static Map<String, String>registeredMap = new HashMap<String, String>(); 

    public Register() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("user");
		String password = request.getParameter("pass");
		
		if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
			request.setAttribute("message", "Please enter both a username and a password.");
			request.getRequestDispatcher(FAILURE_JSP).forward(request, response);
		}
		else if (registeredMap.containsKey(username)) {
			request.setAttribute("message", "This username is taken.");
			request.getRequestDispatcher(FAILURE_JSP).forward(request, response);
		}
		else {
			registeredMap.put(username, password);
			request.setAttribute("message", "Successfully registered.");
			request.getRequestDispatcher(SUCCESS_JSP).forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
