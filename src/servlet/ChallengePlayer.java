package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ChallengePlayer")
public class ChallengePlayer extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SUCCESS_JSP = "/WEB-INF/jsp/success.jsp";
	private static final String FAILURE_JSP = "/WEB-INF/jsp/fail.jsp";
	private static final String CHALLENGE_FORM_JSP = "/WEB-INF/jsp/challenge-form.jsp";
	
	@Override
	public void init(javax.servlet.ServletConfig config) throws ServletException {
		try {
			String key = "";
			MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory(null, null, null, null);
			connectionFactory.defaultInitialization();
			DbRegistry.setConFactory(key, connectionFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public ChallengePlayer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			long challenger = 0;
			try {
				challenger = (long) request.getSession(true).getAttribute("userid");
			}
			catch (NullPointerException e) {
				response.sendRedirect(request.getContextPath() + "/Login");
			}
			
			List<UserRDG> userRDGs = UserRDG.findAll();
			List<String> usernames = new ArrayList<String>();
			for (UserRDG userRDG : userRDGs) {
				if (userRDG.getId() != challenger) {
					usernames.add(userRDG.getUsername());
				}
			}
			
			request.setAttribute("usernames", usernames);
			request.getRequestDispatcher(CHALLENGE_FORM_JSP).forward(request, response);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			String username = request.getParameter("user");
			
			long challenger = (long) request.getSession(true).getAttribute("userid");
			long challengee = UserRDG.findByUsername(username).getId();
			
			if (challenger == challengee) {
				request.setAttribute("message", "You have challenged yourself. You are not allowed to do that");
				request.getRequestDispatcher(FAILURE_JSP).forward(request, response);
			}
			else {
				ChallengeRDG challengeRDG = new ChallengeRDG(ChallengeRDG.getMaxId(), challenger, challengee);
				challengeRDG.insert();
				
				request.setAttribute("message", String.format("You have successfuly challenged %s to a match.", username));
				request.getRequestDispatcher(SUCCESS_JSP).forward(request, response);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
