package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.mapper.CardInPlayInputMapper;
import dom.model.game.Game;
import dom.model.user.User;

public class ViewHandCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your hand.";
	
	@SetInRequestAttribute
	public List<ICardInPlay> hand;
	
	public ViewHandCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			Game game = getGame();
			
			long userId = getUserId();
			User player = userId == game.getChallenger().getId() ? (User) game.getChallenger() : (User) game.getChallengee();
			
			hand = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
					game.getId(), player.getId(), CardStatus.hand.ordinal()
			);
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
