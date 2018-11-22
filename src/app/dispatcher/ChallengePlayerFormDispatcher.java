package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ChallengePlayerFormCommand;

public class ChallengePlayerFormDispatcher extends AbstractDispatcher {
		
	public ChallengePlayerFormDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		try {
			new ChallengePlayerFormCommand(myHelper).execute();
			forward(Global.CHALLENGE_FORM);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		doGet();
	}

}
