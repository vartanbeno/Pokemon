package app.dispatcher;

import java.io.IOException;

import javax.servlet.ServletException;

import org.dsrg.soenea.application.servlet.dispatcher.Dispatcher;

import app.Global;

public abstract class AbstractDispatcher extends Dispatcher {
	
	public abstract void doGet() throws IOException, ServletException;
	
	@Override
	public abstract void execute() throws ServletException, IOException;
	
	protected void success() throws IOException, ServletException {
		forward(Global.SUCCESS);
	}
	
	protected void fail() throws IOException, ServletException {
		forward(Global.FAILURE);
	}

}
