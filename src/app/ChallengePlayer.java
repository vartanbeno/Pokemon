package app;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.ChallengeHelper;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.deck.rdg.DeckRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ChallengePlayer")
public class ChallengePlayer extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SAME_ID = "You have challenged yourself. You are not allowed to do that.";
	private static final String ALREADY_CHALLENGED = "You already have an open challenge against %s.";
	private static final String CHALLENGEE_DOES_NOT_EXIST = "You have issued a challenge to a user that doesn't exist.";
	private static final String NOT_LOGGED_IN = "You must be logged in to issue a challenge to a player.";
	private static final String NO_DECK = "You must have a deck to issue a challenge to a player.";
	
	private static final String CHALLENGE_SUCCESS = "You have successfully challenged %s to a game.";

    public ChallengePlayer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
					
		try {
			
			if (loggedIn(request)) {
				
				final long challenger = getUserId(request);
				DeckRDG deck = DeckRDG.findByPlayer(challenger);
				
				if (deck == null) {
					failure(request, response, NO_DECK);
				}
				else {
					
					List<UserRDG> userRDGs = UserRDG.findAll();
					List<UserHelper> users = new ArrayList<UserHelper>();
					
					for (UserRDG userRDG : userRDGs) {
						users.add(
							new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "")
						);
					}
					
					users.removeIf(user -> user.getId() == challenger);
					
					request.setAttribute("users", users);
					request.getRequestDispatcher(Global.CHALLENGE_FORM).forward(request, response);
					
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
				long challengerId = getUserId(request);
				long challengeeId = Long.parseLong(request.getParameter("player"));
				
				if (challengerId == challengeeId) {
					failure(request, response, SAME_ID);
					return;
				}
				
				ChallengeRDG challengeRDG = ChallengeRDG.findOpenByChallengerAndChallengee(challengerId, challengeeId);
				UserRDG userRDGChallenger = UserRDG.findById(challengerId);
				UserRDG userRDGChallengee = UserRDG.findById(challengeeId);
				
				if (challengeRDG == null) {
					challengeRDG = new ChallengeRDG(ChallengeRDG.getMaxId(), challengerId, challengeeId);
					try {
						challengeRDG.insert();
						success(request, response, String.format(CHALLENGE_SUCCESS, userRDGChallengee.getUsername()));
					}
					catch (NullPointerException | SQLIntegrityConstraintViolationException e) {
						failure(request, response, CHALLENGEE_DOES_NOT_EXIST);
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
					
					failure(request, response, String.format(ALREADY_CHALLENGED, challenge.getChallengee().getUsername()));
					
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
