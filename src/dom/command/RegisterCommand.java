package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.user.IUser;
import dom.model.user.UserFactory;
import dom.model.user.mapper.UserInputMapper;

public class RegisterCommand extends AbstractCommand {
	
	private static final String ENTER_USER_AND_PASS = "Please enter both a username and a password.";
	private static final String USERNAME_TAKEN = "The username %s is taken.";
	
	private static final String REGISTER_SUCCESS = "Successfully registered as %s.";
		
	public RegisterCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			String username = helper.getString("user");
			String password = helper.getString("pass");
			
			if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
				throw new CommandException(ENTER_USER_AND_PASS);
			}
			
			IUser user = UserInputMapper.findByUsername(username);
			if (user != null) {
				throw new CommandException(String.format(USERNAME_TAKEN, username));
			}
			
			user = UserFactory.createNew(username, password);
			
			helper.setSessionAttribute("userid", user.getId());
			
			this.message = String.format(REGISTER_SUCCESS, user.getUsername());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
