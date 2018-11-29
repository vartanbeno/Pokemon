package dom.command;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.command.impl.annotation.SetInRequestAttribute;
import org.dsrg.soenea.domain.helper.Helper;

import dom.model.discard.IDiscard;
import dom.model.discard.mapper.DiscardInputMapper;
import dom.model.game.IGame;

public class ViewDiscardPileCommand extends AbstractCommand {
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view your hand.";
	
	private static final String NOT_PART_OF_GAME = "The player specified is not part of this game.";
	
	@SetInRequestAttribute(attributeName = "discard")
	public List<IDiscard> discardPile;
	
	public ViewDiscardPileCommand(Helper helper) {
		super(helper);
	}

	@Override
	public void process() throws CommandException {
		
		try {
			
			checkIfLoggedIn(NOT_LOGGED_IN);
			
			long gameId = Long.parseLong((String) helper.getRequestAttribute("game"));
			IGame game = getGame(gameId);
			
			checkIfImPartOfGame(game);
			
			long playerId = Long.parseLong((String) helper.getRequestAttribute("player"));
			
			if (playerId != game.getChallenger().getId() && playerId != game.getChallengee().getId())
				throw new CommandException(NOT_PART_OF_GAME);
			
			discardPile = DiscardInputMapper.findByGameAndPlayer(game.getId(), playerId);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		
	}

}
