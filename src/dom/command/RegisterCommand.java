package dom.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.helper.Helper;
import org.dsrg.soenea.uow.UoW;

import dom.model.user.User;
import dom.model.user.UserFactory;
import dom.model.user.mapper.UserInputMapper;

public class RegisterCommand extends AbstractCommand {
	
	private static final String ENTER_USER_AND_PASS = "Please enter both a username and a password.";
	private static final String USERNAME_TAKEN = "The username %s is taken.";
		
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
			
			User user = UserInputMapper.findByUsername(username);
			if (user != null) {
				throw new CommandException(String.format(USERNAME_TAKEN, username));
			}
			
			user = UserFactory.createNew(username, password);
			UoW.getCurrent().commit();
			
			helper.setSessionAttribute("userid", user.getId());
			
		}
		catch (Exception e) {
			throw new CommandException(e);
		}
		
	}

}
