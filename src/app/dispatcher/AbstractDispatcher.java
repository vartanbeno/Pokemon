package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.domain.helper.Helper;

import app.Global;

public abstract class AbstractDispatcher extends Dispatcher {

	@Override
	public abstract void execute() throws ServletException, IOException;
	
	protected void success(Helper helper, String message) throws IOException, ServletException {
		myHelper.setRequestAttribute("message", message);
		forward(Global.SUCCESS);
	}
	
	protected void fail(Helper helper, String message) throws IOException, ServletException {
		myHelper.setRequestAttribute("message", message);
		forward(Global.FAILURE);
	}

}
