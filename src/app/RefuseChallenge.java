package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.mapper.ChallengeOutputMapper;

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
			
			Challenge challenge = getChallengeToRefuseOrWithdraw(request, response);
			if (challenge == null) return;
			
			/**
			 * If you are the one being challenged and refuse, set the challenge status to refused.
			 * Else if you are the challenger and refuse, set the challenge status to withdrawn.
			 */
			if (challenge.getChallengee().getId() == userId) {
				challenge.setStatus(ChallengeStatus.refused.ordinal());
				ChallengeOutputMapper.updateStatic(challenge);
				success(request, response, String.format(REFUSE_SUCCESS, challenge.getChallenger().getUsername()));
			}
			else if (challenge.getChallenger().getId() == userId) {
				challenge.setStatus(ChallengeStatus.withdrawn.ordinal());
				ChallengeOutputMapper.updateStatic(challenge);
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
