package app;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.Challenge;
import dom.model.challenge.ChallengeStatus;
import dom.model.challenge.mapper.ChallengeMapper;
import dom.model.deck.Deck;
import dom.model.deck.IDeck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.game.Game;
import dom.model.game.GameStatus;
import dom.model.game.mapper.GameMapper;
import dom.model.game.tdg.GameTDG;

@WebServlet("/AcceptChallenge")
public class AcceptChallenge extends PageController {
	
	private static final long serialVersionUID = 1L;
	
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
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			List<IDeck> challengeeDecks = getMyDecks(request);
			
			if (challengeeDecks.size() == 0) {
				failure(request, response, NO_DECK);
				return;
			}
				
			Challenge challenge = getChallengeToAccept(request, response);
			if (challenge == null) return;
			
			Deck challengeeDeck = getDeck(request, response);
			if (challengeeDeck == null) return;
			
			Deck challengerDeck = DeckInputMapper.findById(challenge.getChallengerDeck().getId());
			
			if (challenge.getChallengee().getId() == getUserId(request)) {
				
				challenge.setStatus(ChallengeStatus.accepted.ordinal());
				
				Game game = new Game(
						GameTDG.getMaxId(), 1,
						challenge.getChallenger(),
						challenge.getChallengee(),
						challengerDeck,
						challengeeDeck,
						GameStatus.ongoing.ordinal()
				);
				
				ChallengeMapper.updateStatic(challenge);
				GameMapper.insertStatic(game);
				
				success(request, response, String.format(ACCEPT_SUCCESS, challenge.getChallenger().getUsername()));
				
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
