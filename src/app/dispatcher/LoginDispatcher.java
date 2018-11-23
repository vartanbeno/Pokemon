package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.LoginCommand;

public class LoginDispatcher extends AbstractDispatcher {
		
	public LoginDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}

	@Override
	public void doGet() throws IOException, ServletException {
		try {
			forward(Global.LOGIN_FORM);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		try {
			myRequest.getSession(true).invalidate();
			new LoginCommand(myHelper).execute();
			success();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
