package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.user.IUser;
import dom.model.user.mapper.UserInputMapper;

public class ListPlayersCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view the list of players.";
	
	@SetInRequestAttribute
	public List<IUser> players;
	
	public ListPlayersCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			checkIfLoggedIn(NOT_LOGGED_IN);
			players = UserInputMapper.findAll();
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
