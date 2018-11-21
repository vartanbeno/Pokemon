package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ChallengePlayerCommand;
import dom.command.ChallengePlayerFormCommand;

public class ChallengePlayerDispatcher extends AbstractDispatcher {
		
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
			fail();
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new ChallengePlayerCommand(myHelper).execute();
			success();
		}
		catch (Exception e) {
			fail();
		}
		
	}

}
