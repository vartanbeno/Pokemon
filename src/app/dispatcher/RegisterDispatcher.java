package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;

import app.Global;
import dom.command.RegisterCommand;

public class RegisterDispatcher extends Dispatcher {
	
	private static final String REGISTRATION_SUCCESS = "Successfully registered.";
	
	public RegisterDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
						
			new RegisterCommand(myHelper).execute();
						
			myHelper.setRequestAttribute("message", REGISTRATION_SUCCESS);
			forward(Global.SUCCESS);
			
		}
		catch (Exception e) {
			myHelper.setRequestAttribute("message", e.getMessage());
			forward(Global.FAILURE);
		}
		
	}

}
