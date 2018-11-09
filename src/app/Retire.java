package app;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.game.GameStatus;
import dom.model.game.rdg.GameRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/Retire")
public class Retire extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to retire from a game.";
    
	private static final String RETIRE_SUCCESS = "You have successfully retired from your game against %s.";
	
    public Retire() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			GameRDG gameRDG = getGame(request, response);
			if (gameRDG == null) return;
			
			long userId = getUserId(request);
			UserRDG opponentRDG = null;
			
			if (userId == gameRDG.getChallenger()) {
				gameRDG.setStatus(GameStatus.challengerRetired.ordinal());
				opponentRDG = UserRDG.findById(gameRDG.getChallengee());
			}
			else if (userId == gameRDG.getChallengee()) {
				gameRDG.setStatus(GameStatus.challengeeRetired.ordinal());
				opponentRDG = UserRDG.findById(gameRDG.getChallenger());
			}
			
			gameRDG.update();
			
			UserHelper opponent = new UserHelper(
					opponentRDG.getId(), opponentRDG.getVersion(), opponentRDG.getUsername(), ""
			);
			
			success(request, response, String.format(RETIRE_SUCCESS, opponent.getUsername()));
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

}
