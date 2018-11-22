package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

import app.FrontController;
import dom.command.WithdrawFromChallengeCommand;

public class WithdrawFromChallengeDispatcher extends AbstractDispatcher {
	
	public WithdrawFromChallengeDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		try {
			myResponse.sendRedirect(myRequest.getContextPath() + FrontController.OPEN_CHALLENGES);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		try {
			new WithdrawFromChallengeCommand(myHelper).execute();;
			UoW.getCurrent().commit();
			success();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
