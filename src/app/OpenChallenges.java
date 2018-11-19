package app;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.IChallenge;
import dom.model.challenge.mapper.ChallengeInputMapper;
import dom.model.deck.IDeck;

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
			
			List<IDeck> decks = getMyDecks(request);
			List<IChallenge> challengesAgainstMe = ChallengeInputMapper.findOpenByChallengee(userId);
			List<IChallenge> challengesAgainstOthers = ChallengeInputMapper.findOpenByChallenger(userId);
			
			request.setAttribute("decks", decks);
			request.setAttribute("challengesAgainstMe", challengesAgainstMe);
			request.setAttribute("challengesAgainstOthers", challengesAgainstOthers);
			
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
