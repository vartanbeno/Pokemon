package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutDispatcher extends AbstractDispatcher {
	
	private static final String LOGOUT_SUCCESS = "Successfully logged out.";
	
	public LogoutDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}

	@Override
	public void doGet() throws IOException, ServletException {
		execute();
	}

	@Override
	public void execute() throws ServletException, IOException {
		myRequest.getSession().invalidate();
		success(myHelper, LOGOUT_SUCCESS);
	}

}
