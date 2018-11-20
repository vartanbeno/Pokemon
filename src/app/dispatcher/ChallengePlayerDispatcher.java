package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ChallengePlayerCommand;
import dom.command.ChallengePlayerFormCommand;

public class ChallengePlayerDispatcher extends AbstractDispatcher {
	
	private static final String CHALLENGE_SUCCESS = "You have successfully challenged %s to a game.";
	
	public ChallengePlayerDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		
		try {
			new ChallengePlayerFormCommand(myHelper).execute();
			forward(Global.CHALLENGE_FORM);
		}
		catch (Exception e) {
			fail(myHelper, e.getMessage());
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new ChallengePlayerCommand(myHelper).execute();
			success(myHelper, String.format(CHALLENGE_SUCCESS, myHelper.getRequestAttribute("username")));
		}
		catch (Exception e) {
			fail(myHelper, e.getMessage());
		}
		
	}

}
