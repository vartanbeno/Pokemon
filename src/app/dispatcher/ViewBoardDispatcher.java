package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ViewBoardCommand;

public class ViewBoardDispatcher extends AbstractDispatcher {
	
	public ViewBoardDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		try {
			new ViewBoardCommand(myHelper).execute();
			forward(Global.VIEW_BOARD);
		}
		catch (Exception e) {
			fail(myHelper, e.getMessage());
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		doGet();
	}

}
