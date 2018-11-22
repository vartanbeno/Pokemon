package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

public class LogoutCommand extends AbstractCommand {
	
	private static final String LOGOUT_FAIL = "You must be logged in to log out.";
	
	private static final String LOGOUT_SUCCESS = "Successfully logged out.";
	
	public LogoutCommand(Helper helper) {
		super(helper);
	}
	
	@Override
	public void process() throws CommandException {
		try {
			checkIfLoggedIn(LOGOUT_FAIL);
			this.message = LOGOUT_SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}

}
