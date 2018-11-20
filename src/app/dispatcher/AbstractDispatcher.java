package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;
import org.dsrg.soenea.domain.helper.Helper;

import app.Global;

public abstract class AbstractDispatcher extends Dispatcher {
	
	public abstract void doGet() throws IOException, ServletException;
	
	@Override
	public abstract void execute() throws ServletException, IOException;
	
	protected void success() throws IOException, ServletException {
		forward(Global.SUCCESS);
	}
	
	protected void success(Helper helper, String message) throws IOException, ServletException {
		myHelper.setRequestAttribute("message", message);
		forward(Global.SUCCESS);
	}
	
	protected void fail() throws IOException, ServletException {
		forward(Global.FAILURE);
	}
	
	protected void fail(Helper helper, String message) throws IOException, ServletException {
		myHelper.setRequestAttribute("message", message);
		forward(Global.FAILURE);
	}

}
