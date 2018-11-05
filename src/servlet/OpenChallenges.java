package servlet;

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
       
    public OpenChallenges() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request, response)) {
				
				long challengee = (long) request.getSession(true).getAttribute("userid");
				
				List<ChallengeRDG> challengeRDGs = ChallengeRDG.findOpenByChallengee(challengee);
				
				List<ChallengeHelper> challenges = new ArrayList<ChallengeHelper>();
				ChallengeHelper challenge = null;
				
				UserRDG userRDGChallenger = null;
				UserRDG userRDGChallengee = UserRDG.findById(challengee);
				
				UserHelper userChallenger = null;
				UserHelper userChallengee = new UserHelper(
						userRDGChallengee.getId(),
						userRDGChallengee.getVersion(),
						userRDGChallengee.getUsername(),
						""
				);
				
				for (ChallengeRDG challengeRDG : challengeRDGs) {
					
					userRDGChallenger = UserRDG.findById(challengeRDG.getChallenger());
					userChallenger = new UserHelper(
							userRDGChallenger.getId(),
							userRDGChallenger.getVersion(),
							userRDGChallenger.getUsername(),
							""
					);
					
					challenge = new ChallengeHelper(
							challengeRDG.getId(),
							userChallenger,
							userChallengee,
							challengeRDG.getStatus()
					);
					challenges.add(challenge);
					
				}
								
				request.setAttribute("challenges", challenges);
				request.getRequestDispatcher(Global.OPEN_CHALLENGES_FORM).forward(request, response);
				
			}
			else {
				failure(request, response, "You must be logged in to view your open challenges.");
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
		doGet(request, response);
	}

}
