package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;

import app.Global;

public abstract class AbstractDispatcher extends Dispatcher {
	
	public abstract void doGet() throws IOException, ServletException;
	
	@Override
	public abstract void execute() throws ServletException, IOException;
	
	/**
	 * The message request attribute is set in the command.
	 * Commands can be found in src.dom.command
	 * 
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void success() throws IOException, ServletException {
		forward(Global.SUCCESS);
	}
	
	protected void fail() throws IOException, ServletException {
		forward(Global.FAILURE);
	}
	
	/**
	 * Need this because if the UoW fails to commit, we need to catch the message.
	 * We commit the UoW in the dispatcher, not the command.
	 * 
	 * @param message: a useful message to be shown to the user.
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void fail(String message) throws IOException, ServletException {
		myHelper.setRequestAttribute("message", message);
		fail();
	}

}
