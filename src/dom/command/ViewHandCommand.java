package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.game.Game;
import dom.model.hand.IHand;
import dom.model.hand.mapper.HandInputMapper;
import dom.model.user.User;

public class ViewHandCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your hand.";
	
	@SetInRequestAttribute
	public List<IHand> hand;
	
	public ViewHandCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			Game game = getGame(gameId);
			
			checkIfImPartOfGame(game);
			
			long userId = getUserId();
			User player = userId == game.getChallenger().getId() ? (User) game.getChallenger() : (User) game.getChallengee();
			
			hand = HandInputMapper.findByGameAndPlayer(game.getId(), player.getId());
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
