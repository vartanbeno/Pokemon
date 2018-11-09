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
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			long userId = getUserId(request);
			
			ChallengeRDG challengeRDG = getChallengeToRefuseOrWithdraw(request, response);
			if (challengeRDG == null) return;
			
			UserRDG userRDGChallenger = UserRDG.findById(challengeRDG.getChallenger());
			UserRDG userRDGChallengee = UserRDG.findById(challengeRDG.getChallengee());
			
			UserHelper challenger = new UserHelper(
					userRDGChallenger.getId(),
					userRDGChallenger.getVersion(),
					userRDGChallenger.getUsername(),
					""
			);
			
			UserHelper challengee = new UserHelper(
					userRDGChallengee.getId(),
					userRDGChallengee.getVersion(),
					userRDGChallengee.getUsername(),
					""
			);
			
			ChallengeHelper challenge = new ChallengeHelper(
					challengeRDG.getId(),
					challenger,
					challengee,
					challengeRDG.getStatus()
			);
			
			/**
			 * If you are the one being challenged and refuse, set the challenge status to refused.
			 * Else if you are the challenger and refuse, set the challenge status to withdrawn.
			 */
			if (challengeRDG.getChallengee() == userId) {
				challengeRDG.setStatus(ChallengeStatus.refused.ordinal());
				challengeRDG.update();
				success(request, response, String.format(REFUSE_SUCCESS, challenge.getChallenger().getUsername()));
			}
			else if (challengeRDG.getChallenger() == userId) {
				challengeRDG.setStatus(ChallengeStatus.withdrawn.ordinal());
				challengeRDG.update();
				success(request, response, String.format(WITHDRAW_SUCCESS, challenge.getChallengee().getUsername()));
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
