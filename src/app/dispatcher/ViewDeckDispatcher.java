package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ViewDeckCommand;

public class ViewDeckDispatcher extends AbstractDispatcher {
	
	public ViewDeckDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}

	@Override
	public void doGet() throws IOException, ServletException {
		try {
			new ViewDeckCommand(myHelper).execute();
			forward(Global.VIEW_DECK);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		doGet();
	}

}
