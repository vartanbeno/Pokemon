package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ChallengePlayer")
public class ChallengePlayer extends PageController {
	
	private static final long serialVersionUID = 1L;

    public ChallengePlayer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
						
			Long challenger = null;
			try {
				challenger = (long) request.getSession(true).getAttribute("userid");
				
				List<UserRDG> userRDGs = UserRDG.findAll();
				List<String> usernames = new ArrayList<String>();
				for (UserRDG userRDG : userRDGs) {
					if (userRDG.getId() != challenger) {
						usernames.add(userRDG.getUsername());
					}
				}
				
				request.setAttribute("usernames", usernames);
				request.getRequestDispatcher(Global.CHALLENGE_FORM).forward(request, response);
			}
			catch (NullPointerException e) {
				response.sendRedirect(request.getContextPath() + "/Login");
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
						
			String username = request.getParameter("user");
			
			long challenger = (long) request.getSession(true).getAttribute("userid");
			UserRDG challengee = UserRDG.findByUsername(username);
			
			if (challenger == challengee.getId()) {
				failure(request, response, "You have challenged yourself. You are not allowed to do that");
			}
			else {
				ChallengeRDG challengeRDG = new ChallengeRDG(ChallengeRDG.getMaxId(), challenger, challengee.getId());
				challengeRDG.insert();
				success(request, response, String.format("You have successfuly challenged %s to a match.", challengee.getUsername()));
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
