package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

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
			fail(e.getMessage());
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new ChallengePlayerCommand(myHelper).execute();
			UoW.getCurrent().commit();
			success();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
	}

}
