package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.ChallengeHelper;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/OpenChallenges")
public class OpenChallenges extends PageController {

	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your open challenges.";
       
    public OpenChallenges() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
				
			long userId = getUserId(request);
			
			List<ChallengeRDG> challengeeChallengeRDGs = ChallengeRDG.findOpenByChallengee(userId);
			List<ChallengeRDG> challengerChallengeRDGs = ChallengeRDG.findOpenByChallenger(userId);
			
			List<ChallengeHelper> challengeeChallenges = new ArrayList<ChallengeHelper>();
			List<ChallengeHelper> challengerChallenges = new ArrayList<ChallengeHelper>();
			
			UserRDG user = UserRDG.findById(userId);
			UserHelper userHelper = new UserHelper(
					user.getId(),
					user.getVersion(),
					user.getUsername(),
					""
			);
			
			for (ChallengeRDG challengeRDG : challengeeChallengeRDGs) {
				
				UserRDG someoneElse = UserRDG.findById(challengeRDG.getChallenger());
				UserHelper someoneElseHelper = new UserHelper(
						someoneElse.getId(),
						someoneElse.getVersion(),
						someoneElse.getUsername(),
						""
				);
				
				ChallengeHelper challenge = new ChallengeHelper(
						challengeRDG.getId(),
						someoneElseHelper,
						userHelper,
						challengeRDG.getStatus()
				);
				
				challengeeChallenges.add(challenge);
				
			}
			
			for (ChallengeRDG challengeRDG : challengerChallengeRDGs) {
				
				UserRDG someoneElse = UserRDG.findById(challengeRDG.getChallengee());
				UserHelper someoneElseHelper = new UserHelper(
						someoneElse.getId(),
						someoneElse.getVersion(),
						someoneElse.getUsername(),
						""
				);
				
				ChallengeHelper challenge = new ChallengeHelper(
						challengeRDG.getId(),
						userHelper,
						someoneElseHelper,
						challengeRDG.getStatus()
				);
				
				challengerChallenges.add(challenge);
				
			}
			
			request.setAttribute("challengesAgainstMe", challengeeChallenges);
			request.setAttribute("challengesAgainstOthers", challengerChallenges);
			request.getRequestDispatcher(Global.OPEN_CHALLENGES_FORM).forward(request, response);
    		
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	finally {
			closeDb();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
