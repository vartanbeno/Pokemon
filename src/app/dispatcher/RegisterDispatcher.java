package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

import app.Global;
import dom.command.RegisterCommand;

public class RegisterDispatcher extends AbstractDispatcher {
		
	public RegisterDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		forward(Global.REGISTER_FORM);
	}

	@Override
	public void execute() throws ServletException, IOException {
		try {
			myRequest.getSession(true).invalidate();
			new RegisterCommand(myHelper).execute();
			UoW.getCurrent().commit();
			success();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
