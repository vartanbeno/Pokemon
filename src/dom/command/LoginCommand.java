package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class LoginCommand extends AbstractCommand {
	
	private static final String ENTER_USER_AND_PASS = "Please enter both a username and a password.";
	private static final String INVALID_CREDENTIALS = "Incorrect username and/or password.";
	
	public LoginCommand(Helper helper) {
		super(helper);
	}
	
	@Override
	public void process() throws CommandException {
		
		try {
			
			String username = helper.getString("user");
			String password = helper.getString("pass");
			
			if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
				throw new CommandException(ENTER_USER_AND_PASS);
			}
			
			User user = UserInputMapper.findByUsernameAndPassword(username, password);
			if (user == null) throw new CommandException(INVALID_CREDENTIALS);
			
			helper.setSessionAttribute("userid", user.getId());
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}
	
}
