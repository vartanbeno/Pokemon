package app;

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
	
	private static final String REFUSE_SUCCESS = "You have successfully refused %s's challenge.";
	private static final String WITHDRAW_SUCCESS = "You have successfully withdrawn from your challenge against %s.";
	
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
				long challengeId = Long.parseLong(request.getParameter("challenge"));
				
				ChallengeRDG challengeRDG = ChallengeRDG.findOpenById(challengeId);
				
				if (challengeRDG == null) {
					failure(request, response, CHALLENGE_DOES_NOT_EXIST);
					return;
				}
				
				UserRDG userRDGChallenger = UserRDG.findById(challengeRDG.getChallenger());
				UserRDG userRDGChallengee = UserRDG.findById(challengeRDG.getChallengee());
				
				if (userRDGChallengee == null) {
					failure(request, response, CHALLENGER_DOES_NOT_EXIST);
					return;
				}
				
				UserHelper challenger = new UserHelper(
						userRDGChallenger.getId(), userRDGChallenger.getVersion(), userRDGChallenger.getUsername(), ""
				);
				
				UserHelper challengee = new UserHelper(
						userRDGChallengee.getId(), userRDGChallengee.getVersion(), userRDGChallengee.getUsername(), ""
				);
				
				ChallengeHelper challenge = new ChallengeHelper(
						challengeRDG.getId(), challenger, challengee, challengeRDG.getStatus()
				);
				
				/**
				 * If you are the one being challenged and refuse, set the challenge status to refused.
				 * If you are the one who challenged a player and refuse, set the challenge status to withdrawn.
				 */
				if (challengeRDG.getChallengee() == challengeeId) {
					challengeRDG.setStatus(ChallengeStatus.refused.ordinal());
					challengeRDG.update();
					success(request, response, String.format(REFUSE_SUCCESS, challenge.getChallenger().getUsername()));
				}
				else if (challengeRDG.getChallenger() == challengeeId) {
					challengeRDG.setStatus(ChallengeStatus.withdrawn.ordinal());
					challengeRDG.update();
					success(request, response, String.format(WITHDRAW_SUCCESS, challenge.getChallengee().getUsername()));
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
