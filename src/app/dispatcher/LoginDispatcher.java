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
		forward(Global.LOGIN_FORM);
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			myRequest.getSession().invalidate();
			new LoginCommand(myHelper).execute();
			success();
		}
		catch (Exception e) {
			fail();
		}
		
	}

}
