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

public class ViewDiscardPileCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your hand.";
	
	private static final String NOT_PART_OF_GAME = "The player specified is not part of this game.";
	
	@SetInRequestAttribute
	public List<ICardInPlay> hand;
	
	public ViewDiscardPileCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			Game game = getGame(gameId);
			
			long playerId = Long.parseLong((String) helper.getRequestAttribute("player"));
			User player = null;
			
			if (playerId == game.getChallenger().getId()) {
				player = (User) game.getChallenger();
			}
			else if (playerId == game.getChallengee().getId()) {
				player = (User) game.getChallengee();
			}
			
			if (player == null) throw new CommandException(NOT_PART_OF_GAME);
			
			hand = CardInPlayInputMapper.findByGameAndPlayerAndStatus(
					game.getId(), player.getId(), CardStatus.discarded.ordinal()
			);
			
		}
		catch (Exception e) {
			throw new CommandException(e.getMessage());
		}
		
	}

}
