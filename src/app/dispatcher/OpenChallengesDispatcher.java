package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.OpenChallengesCommand;

public class OpenChallengesDispatcher extends AbstractDispatcher {
	
	public OpenChallengesDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		
		try {
			new OpenChallengesCommand(myHelper).execute();
			forward(Global.OPEN_CHALLENGES_FORM);
		}
		catch (Exception e) {
			fail(myHelper, e.getMessage());
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException {
		doGet();
	}

}
