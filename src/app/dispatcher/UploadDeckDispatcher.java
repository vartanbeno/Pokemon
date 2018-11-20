package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.Global;
import dom.command.UploadDeckCommand;
import dom.command.UploadDeckFormCommand;

public class UploadDeckDispatcher extends AbstractDispatcher {
	
	private static final String DECK_SUCCESS_MESSAGE = "You have successfully uploaded your deck.";
	
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
			fail(myHelper, e.getMessage());
		}
		
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new UploadDeckCommand(myHelper).execute();
			success(myHelper, DECK_SUCCESS_MESSAGE);
		}
		catch (Exception e) {
			fail(myHelper, e.getMessage());
		}
		
	}

}
