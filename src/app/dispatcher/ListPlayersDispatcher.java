package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.ListPlayersCommand;

public class ListPlayersDispatcher extends AbstractDispatcher {
	
	public ListPlayersDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		
		try {
			new ListPlayersCommand(myHelper).execute();
			forward(Global.LIST_PLAYERS);
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
