package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.rdg.UserRDG;

@WebServlet("/AcceptChallenge")
public class AcceptChallenge extends PageController {
	
	private static final long serialVersionUID = 1L;

    public AcceptChallenge() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath() + "/OpenChallenges");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request, response)) {
				
				long challengeeId = (long) request.getSession(true).getAttribute("userid");
				long challengerId = Long.parseLong(request.getParameter("challenger"));
				
				UserRDG challenger = UserRDG.findById(challengerId);
				ChallengeRDG challenge = ChallengeRDG.findOpenByChallengerAndChallengee(challengerId, challengeeId);
				
				if (challengerId == challengeeId) {
					failure(request, response, "You have accepted a challenge against yourself. You are not allowed to do that");
				}
				else if (challenge == null) {
					failure(request, response, String.format("An open challenge against %s does not exist.", challenger.getUsername()));
				}
				else {
					challenge.setStatus(ChallengeStatus.accepted.ordinal());
					challenge.update();
					success(request, response, String.format("You have successfully accepted %s's challenge.", challenger.getUsername()));
				}
				
			}
			else {
				failure(request, response, "You must be logged in to accept a challenge.");
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
