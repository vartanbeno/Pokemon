package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.game.IGame;
import dom.model.game.mapper.GameInputMapper;

public class ListGamesCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view the list of games.";
	
	@SetInRequestAttribute
	public List<IGame> games;

	public ListGamesCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		try {
			checkIfLoggedIn(NOT_LOGGED_IN);
			games = GameInputMapper.findAll();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
	}

}
