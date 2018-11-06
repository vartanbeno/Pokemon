package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.ChallengeHelper;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/RefuseChallenge")
public class RefuseChallenge extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SAME_ID = "You have refused a challenge against yourself. You are not allowed to do that.";
	private static final String CHALLENGE_DOES_NOT_EXIST = "An open challenge against %s does not exist.";
	private static final String CHALLENGER_DOES_NOT_EXIST = "You have refused a challenge against a user that doesn't exist.";
	private static final String NOT_LOGGED_IN = "You must be logged in to refuse a challenge.";
	
	private static final String ACCEPT_SUCCESS = "You have successfully refused %s's challenge.";
       
    public RefuseChallenge() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/OpenChallenges");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
				long challengeeId = getUserId(request);
				long challengerId = Long.parseLong(request.getParameter("challenger"));
				
				if (challengeeId == challengerId) {
					failure(request, response, SAME_ID);
					return;
				}
				
				ChallengeRDG challengeRDG = ChallengeRDG.findOpenByChallengerAndChallengee(challengerId, challengeeId);
				UserRDG userRDGChallenger = UserRDG.findById(challengerId);
				UserRDG userRDGChallengee = UserRDG.findById(challengeeId);
				
				if (challengeRDG == null) {
					try {
						failure(request, response, String.format(CHALLENGE_DOES_NOT_EXIST, userRDGChallenger.getUsername()));
					}
					catch (NullPointerException e) {
						failure(request, response, CHALLENGER_DOES_NOT_EXIST);
					}
				}
				else {
					
					UserHelper challenger = new UserHelper(
							userRDGChallenger.getId(), userRDGChallenger.getVersion(), userRDGChallenger.getUsername(), ""
					);
					
					UserHelper challengee = new UserHelper(
							userRDGChallengee.getId(), userRDGChallengee.getVersion(), userRDGChallengee.getUsername(), ""
					);
					
					ChallengeHelper challenge = new ChallengeHelper(
							challengeRDG.getId(), challenger, challengee, challengeRDG.getStatus()
					);
					
					challengeRDG.setStatus(ChallengeStatus.refused.ordinal());
					challengeRDG.update();
					success(request, response, String.format(ACCEPT_SUCCESS, challenge.getChallenger().getUsername()));
					
				}
				
			}
			else {
				failure(request, response, NOT_LOGGED_IN);
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
