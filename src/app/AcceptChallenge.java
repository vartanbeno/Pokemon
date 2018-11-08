package app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.ChallengeHelper;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.deck.DeckHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/AcceptChallenge")
public class AcceptChallenge extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SAME_ID = "You have accepted a challenge against yourself. You are not allowed to do that.";
	private static final String CHALLENGE_DOES_NOT_EXIST = "An open challenge against %s does not exist.";
	private static final String CHALLENGER_DOES_NOT_EXIST = "You have accepted a challenge against a user that doesn't exist.";
	private static final String NOT_PART_OF_CHALLENGE = "You must be part of the challenge to accept it.";
	private static final String NOT_LOGGED_IN = "You must be logged in to accept a challenge.";
	private static final String NO_DECK = "You must have a deck to accept a challenge.";
	
	private static final String ACCEPT_SUCCESS = "You have successfully accepted %s's challenge. The game has begun!";

    public AcceptChallenge() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath() + "/OpenChallenges");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
				long userId = getUserId(request);
				DeckRDG deck = DeckRDG.findByPlayer(userId);
				
				if (deck == null) {
					failure(request, response, NO_DECK);
				}
				else {
					
					long challengeId = Long.parseLong(request.getParameter("challenge"));
					
					ChallengeRDG challengeRDG = ChallengeRDG.findOpenById(challengeId);
					
					if (challengeRDG == null) {
						failure(request, response, CHALLENGE_DOES_NOT_EXIST);
						return;
					}
					
					UserRDG userRDGChallenger = UserRDG.findById(challengeRDG.getChallenger());
					UserRDG userRDGChallengee = UserRDG.findById(challengeRDG.getChallengee());
					
					try {
						if (userId == userRDGChallenger.getId()) {
							failure(request, response, SAME_ID);
							return;
						}
					}
					catch (NullPointerException e) {
						failure(request, response, CHALLENGER_DOES_NOT_EXIST);
						return;
					}
					
					UserHelper challenger = new UserHelper(
							userRDGChallenger.getId(), userRDGChallenger.getVersion(), userRDGChallenger.getUsername(), ""
					);
					
					DeckRDG challengerDeckRDG = DeckRDG.findByPlayer(challenger.getId());
					DeckHelper challengerDeck = new DeckHelper(challengerDeckRDG.getId(), challenger);
					
					UserHelper challengee = new UserHelper(
							userRDGChallengee.getId(), userRDGChallengee.getVersion(), userRDGChallengee.getUsername(), ""
					);
					
					DeckRDG challengeeDeckRDG = DeckRDG.findByPlayer(challengee.getId());
					DeckHelper challengeeDeck = new DeckHelper(challengeeDeckRDG.getId(), challengee);
					
					ChallengeHelper challenge = new ChallengeHelper(
							challengeRDG.getId(), challenger, challengee, challengeRDG.getStatus()
					);
					
					if (challengeRDG.getChallengee() == userId) {
						
						challengeRDG.setStatus(ChallengeStatus.accepted.ordinal());
						
						GameRDG gameRDG = new GameRDG(
								GameRDG.getMaxId(),
								challenger.getId(),
								challengee.getId(),
								challengerDeck.getId(),
								challengeeDeck.getId()
						);
						
						challengeRDG.update();
						gameRDG.insert();
						
						success(request, response, String.format(ACCEPT_SUCCESS, challenge.getChallenger().getUsername()));
						
					}
					else {
						failure(request, response, NOT_PART_OF_CHALLENGE);
					}
					
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
