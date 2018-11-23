package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ListGamesCommand;

public class ListGamesDispatcher extends AbstractDispatcher {
	
	public ListGamesDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		try {
			new ListGamesCommand(myHelper).execute();
			forward(Global.LIST_GAMES);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		doGet();
	}

}
