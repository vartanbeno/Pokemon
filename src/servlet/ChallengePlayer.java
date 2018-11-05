package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ChallengePlayer")
public class ChallengePlayer extends PageController {
	
	private static final long serialVersionUID = 1L;

    public ChallengePlayer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
					
		try {
			
			if (loggedIn(request, response)) {
				
				final long challenger = (long) request.getSession(true).getAttribute("userid");
				
				List<UserRDG> userRDGs = UserRDG.findAll();
				
				List<UserHelper> users = new ArrayList<UserHelper>();
				for (UserRDG userRDG : userRDGs) {
					users.add(
						new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "")
					);
				}
				
				users.removeIf(user -> user.getId() == challenger);
				
				request.setAttribute("users", users);
				request.getRequestDispatcher(Global.CHALLENGE_FORM).forward(request, response);

			}
			else {
				failure(request, response, "You must be logged in to issue a challenge to a player.");
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
			
			if (loggedIn(request, response)) {
				
				long challenger = (long) request.getSession(true).getAttribute("userid");
				long challengee = Long.parseLong(request.getParameter("challengee"));
				
				UserRDG userRDG = UserRDG.findById(challengee);
				ChallengeRDG challengeRDG = ChallengeRDG.findOpenByChallengerAndChallengee(challenger, challengee);
				
				if (challenger == challengee) {
					failure(request, response, "You have challenged yourself. You are not allowed to do that");
				}
				else if (challengeRDG != null) {
					failure(request, response, String.format("You already have an open challenge against %s.", userRDG.getUsername()));
				}
				else {
					challengeRDG = new ChallengeRDG(ChallengeRDG.getMaxId(), challenger, challengee);
					challengeRDG.insert();
					success(request, response, String.format("You have successfully challenged %s to a game.", userRDG.getUsername()));
				}
				
			}
			else {
				failure(request, response, "You must be logged in to issue a challenge to a player.");
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
