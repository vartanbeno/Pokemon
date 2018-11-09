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

@WebServlet("/ListChallenges")
public class ListChallenges extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view the list of challenges.";

    public ListChallenges() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			List<ChallengeRDG> challengeRDGs = ChallengeRDG.findAll();
			List<ChallengeHelper> challenges = new ArrayList<ChallengeHelper>();
			
			for (ChallengeRDG challengeRDG : challengeRDGs) {
				
				UserRDG userRDGChallenger = UserRDG.findById(challengeRDG.getChallenger());
				UserHelper challenger = new UserHelper(
						userRDGChallenger.getId(),
						userRDGChallenger.getVersion(),
						userRDGChallenger.getUsername(),
						""
				);
				
				UserRDG userRDGChallengee = UserRDG.findById(challengeRDG.getChallengee());
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
				
				challenges.add(challenge);
				
			}
			
			request.setAttribute("challenges", challenges);
			request.getRequestDispatcher(Global.LIST_CHALLENGES).forward(request, response);
			
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
