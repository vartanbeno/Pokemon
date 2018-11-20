package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.ValidatorCommand;
import org.dsrg.soenea.domain.helper.Helper;

public abstract class AbstractCommand extends ValidatorCommand {

	public AbstractCommand(Helper helper) {
		super(helper);
	}

	@Override
	public abstract void process() throws CommandException;
	
	protected long getUserId() {
		return (long) helper.getSessionAttribute("userid");
	}
	
	protected void checkIfLoggedIn(String message) throws CommandException {
		
		try {
			getUserId();
		}
		catch (NullPointerException e) {
			throw new CommandException(message);
		}
		
	}

}
