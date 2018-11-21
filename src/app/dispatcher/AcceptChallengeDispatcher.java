package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

import app.FrontController;
import dom.command.AcceptChallengeCommand;

public class AcceptChallengeDispatcher extends AbstractDispatcher {
	
	public AcceptChallengeDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		
		try {
			myResponse.sendRedirect(myRequest.getContextPath() + FrontController.OPEN_CHALLENGES);
		}
		catch (Exception e) {
			fail();
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new AcceptChallengeCommand(myHelper).execute();
			UoW.getCurrent().commit();
			success();
		}
		catch (Exception e) {
			fail();
		}
		
	}

}
