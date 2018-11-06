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

    public ListChallenges() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			List<ChallengeRDG> challengeRDGs = ChallengeRDG.findAll();
			List<ChallengeHelper> challenges = new ArrayList<ChallengeHelper>();
			
			ChallengeHelper challenge = null;
			
			UserRDG userRDGChallenger = null;
			UserRDG userRDGChallengee = null;
			
			UserHelper challenger = null;
			UserHelper challengee = null;
			
			for (ChallengeRDG challengeRDG : challengeRDGs) {
				
				userRDGChallenger = UserRDG.findById(challengeRDG.getChallenger());
				challenger = new UserHelper(
						userRDGChallenger.getId(),
						userRDGChallenger.getVersion(),
						userRDGChallenger.getUsername(),
						""
				);
				
				userRDGChallengee = UserRDG.findById(challengeRDG.getChallengee());
				challengee = new UserHelper(
						userRDGChallengee.getId(),
						userRDGChallengee.getVersion(),
						userRDGChallengee.getUsername(),
						""
				);
				
				challenge = new ChallengeHelper(
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
