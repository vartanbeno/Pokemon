package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.UploadDeckFormCommand;

public class UploadDeckDispatcher extends AbstractDispatcher {
		
	public UploadDeckDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		
		try {
			new UploadDeckFormCommand(myHelper).execute();
			forward(Global.UPLOAD_DECK_FORM);
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
