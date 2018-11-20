package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ViewHandCommand;

public class ViewHandDispatcher extends AbstractDispatcher {
	
	public ViewHandDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}

	@Override
	public void doGet() throws IOException, ServletException {
		try {
			new ViewHandCommand(myHelper).execute();
			forward(Global.VIEW_HAND);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(myHelper, e.getMessage());
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		doGet();
	}

}
