package app;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.game.Game;
import dom.model.game.GameStatus;
import dom.model.game.mapper.GameMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

@WebServlet("/Retire")
public class Retire extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String GAME_ALREADY_ENDED = "This game has already ended.";
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
			
			Game game = getGame(request, response);
			if (game == null) return;
			
			if (game.getStatus() != GameStatus.ongoing.ordinal()) {
				failure(request, response, GAME_ALREADY_ENDED);
				return;
			}
			
			long userId = getUserId(request);
			User opponent = null;
			
			if (userId == game.getChallenger().getId()) {
				game.setStatus(GameStatus.challengerRetired.ordinal());
				opponent = UserInputMapper.findById(game.getChallengee().getId());
			}
			else if (userId == game.getChallengee().getId()) {
				game.setStatus(GameStatus.challengeeRetired.ordinal());
				opponent = UserInputMapper.findById(game.getChallenger().getId());
			}
			
			GameMapper.updateStatic(game);
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
