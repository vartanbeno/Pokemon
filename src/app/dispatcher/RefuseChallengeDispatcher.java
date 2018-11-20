package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.FrontController;
import dom.command.RefuseChallengeCommand;

public class RefuseChallengeDispatcher extends AbstractDispatcher {
	
	public RefuseChallengeDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		
		try {
			myResponse.sendRedirect(myRequest.getContextPath() + FrontController.OPEN_CHALLENGES);
		}
		catch (Exception e) {
			fail(myHelper, e.getMessage());
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new RefuseChallengeCommand(myHelper).execute();
			success();
		}
		catch (Exception e) {
			fail(myHelper, e.getMessage());
		}
		
	}

}
