package app;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.mapper.ChallengeMapper;
import dom.model.challenge.tdg.ChallengeTDG;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.user.IUser;
import dom.model.user.mapper.UserInputMapper;

@WebServlet("/ChallengePlayer")
public class ChallengePlayer extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String SAME_ID = "You have challenged yourself. You are not allowed to do that.";
	private static final String ALREADY_CHALLENGED = "You already have an open challenge against %s.";
	private static final String CHALLENGEE_DOES_NOT_EXIST = "You have issued a challenge to a user that doesn't exist.";
	private static final String CHALLENGEE_ID_FORMAT = "You must provide a valid format for the challengee ID (positive integer).";
	private static final String NOT_LOGGED_IN = "You must be logged in to issue a challenge to a player.";
	private static final String NO_DECK = "You must have at least one deck to issue a challenge to a player.";
	
	private static final String CHALLENGE_SUCCESS = "You have successfully challenged %s to a game.";

    public ChallengePlayer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
					
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
				
			List<IDeck> decks = getMyDecks(request);
			
			if (decks.size() == 0) {
				failure(request, response, NO_DECK);
				return;
			}
			
			List<IUser> challengees = UserInputMapper.findAll();
			
			/**
			 * We want a list of users to be able to challenge.
			 * But we should omit our own username,
			 * because we shouldn't be able to challenge ourselves.
			 */
			challengees.removeIf(challengee -> challengee.getId() == getUserId(request));
			
			request.setAttribute("users", challengees);
			request.setAttribute("decks", decks);
			request.getRequestDispatcher(Global.CHALLENGE_FORM).forward(request, response);
			
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
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			long challengerId = getUserId(request);
			Long challengeeId = null;
			
			try {
				challengeeId = Long.parseLong(request.getParameter("player"));
			}
			catch (NumberFormatException e) {
				failure(request, response, CHALLENGEE_ID_FORMAT);
				return;
			}
			
			if (getMyDecks(request).size() == 0) {
				failure(request, response, NO_DECK);
				return;
			}
			
			if (challengerId == challengeeId) {
				failure(request, response, SAME_ID);
				return;
			}
			
			Challenge challenge = ChallengeMapper.findOpenByChallengerAndChallengee(challengerId, challengeeId);
			
			Deck challengerDeck = getDeck(request, response);
			if (challengerDeck == null) return;
			
			if (challenge == null) {
				challenge = new Challenge(
						ChallengeTDG.getMaxId(), 1,
						UserInputMapper.findById(challengerId),
						UserInputMapper.findById(challengeeId),
						ChallengeStatus.open.ordinal(),
						challengerDeck
				);
				try {
					ChallengeMapper.insertStatic(challenge);
					success(request, response, String.format(CHALLENGE_SUCCESS, challenge.getChallengee().getUsername()));
				}
				catch (NullPointerException | SQLIntegrityConstraintViolationException e) {
					failure(request, response, CHALLENGEE_DOES_NOT_EXIST);
				}
			}
			else {
				failure(request, response, String.format(ALREADY_CHALLENGED, challenge.getChallengee().getUsername()));
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
