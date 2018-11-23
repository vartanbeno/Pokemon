package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

import app.Global;
import dom.command.UploadDeckCommand;
import dom.command.ViewDecksCommand;

public class DecksDispatcher extends AbstractDispatcher {
	
	public DecksDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}

	@Override
	public void doGet() throws IOException, ServletException {
		try {
			new ViewDecksCommand(myHelper).execute();
			forward(Global.VIEW_DECKS);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Override
	public void execute() throws ServletException, IOException {
		try {
			new UploadDeckCommand(myHelper).execute();
			UoW.getCurrent().commit();
			success();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
