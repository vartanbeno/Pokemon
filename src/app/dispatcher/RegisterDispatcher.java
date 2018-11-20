package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.RegisterCommand;

public class RegisterDispatcher extends AbstractDispatcher {
	
	private static final String REGISTRATION_SUCCESS = "Successfully registered as %s.";
	
	public RegisterDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	public void doGet() throws IOException, ServletException {
		forward(Global.REGISTER_FORM);
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new RegisterCommand(myHelper).execute();
			success(myHelper, String.format(REGISTRATION_SUCCESS, myHelper.getString("user")));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(myHelper, e.getMessage());
		}
		
	}

}
