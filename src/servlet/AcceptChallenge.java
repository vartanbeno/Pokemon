package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.ChallengeHelper;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/AcceptChallenge")
public class AcceptChallenge extends PageController {
	
	private static final long serialVersionUID = 1L;

    public AcceptChallenge() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	try {
    		
    		try {
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
				request.getRequestDispatcher(Global.ACCEPT_CHALLENGE_FORM).forward(request, response);
			}
			catch (NullPointerException e) {
				response.sendRedirect(request.getContextPath() + "/Login");
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
			
			long challengeeId = (long) request.getSession(true).getAttribute("userid");
			long challengerId = Long.parseLong(request.getParameter("challenger"));
			UserRDG challenger = UserRDG.findById(challengerId);
			
			ChallengeRDG challenge = ChallengeRDG.findOpenByChallengerAndChallengee(challengerId, challengeeId);
			
			if (challengerId == challengeeId) {
				failure(request, response, "You have challenged yourself. You are not allowed to do that");
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
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

}
