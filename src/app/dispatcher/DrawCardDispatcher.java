package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.DrawCardCommand;

public class DrawCardDispatcher extends AbstractDispatcher {
	
	public DrawCardDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		
		try {
			new DrawCardCommand(myHelper).execute();
			forward(Global.SUCCESS);
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
