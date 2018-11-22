package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.uow.UoW;

import dom.command.PlayPokemonToBenchCommand;

public class PlayPokemonToBenchDispatcher extends AbstractDispatcher {
	
	public PlayPokemonToBenchDispatcher(HttpServletRequest request, HttpServletResponse response) {
		super.init(request, response);
	}
	
	@Override
	public void doGet() throws IOException, ServletException {
		execute();
	}

	@Override
	public void execute() throws ServletException, IOException {
		
		try {
			new PlayPokemonToBenchCommand(myHelper).execute();
			UoW.getCurrent().commit();
			success();
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
	}

}
