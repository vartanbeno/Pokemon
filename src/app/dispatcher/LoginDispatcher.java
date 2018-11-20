package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.LoginCommand;

public class LoginDispatcher extends AbstractDispatcher {
	
	private static final String LOGIN_SUCCESS = "Successfully logged in.";
	
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
			success(myHelper, LOGIN_SUCCESS);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(myHelper, e.getMessage());
		}
		
	}

}
