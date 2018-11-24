package dom.model.hand.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.card.Card;
import dom.model.card.mapper.CardInputMapper;
import dom.model.game.Game;
import dom.model.game.mapper.GameInputMapper;
import dom.model.hand.Hand;
import dom.model.hand.HandFactory;
import dom.model.hand.IHand;
import dom.model.hand.tdg.HandFinder;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class HandInputMapper {
	
	public static List<IHand> findAll() throws SQLException {
		
		ResultSet rs = HandFinder.findAll();
		
		List<IHand> hand = buildHand(rs);
		rs.close();
		
		return hand;
		
	}
	
	public static Hand findById(long id) throws SQLException {
		
		Hand handCard = getFromIdentityMap(id);
		if (handCard != null) return handCard;
		
		ResultSet rs = HandFinder.findById(id);
		
		handCard = rs.next() ? buildHandCard(rs) : null;
		rs.close();
		
		return handCard;
		
	}
	
	public static List<IHand> findByGameAndPlayer(long game, long player) throws SQLException {
		
		ResultSet rs = HandFinder.findByGameAndPlayer(game, player);
		List<IHand> hand = buildHand(rs);
		rs.close();
		
		return hand;
		
	}
	
	public static Hand buildHandCard(ResultSet rs) throws SQLException {
		
		Game game = GameInputMapper.findById(rs.getLong("game"));
		User player = UserInputMapper.findById(rs.getLong("player"));
		Card card = CardInputMapper.findById(rs.getLong("card"));
		
		return HandFactory.createClean(
				rs.getLong("id"),
				rs.getLong("version"),
				game,
				player,
				rs.getLong("deck"),
				card
		);
		
	}
	
	public static List<IHand> buildHand(ResultSet rs) throws SQLException {
		
		List<IHand> hand = new ArrayList<IHand>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			Hand handCard = getFromIdentityMap(id);
			
			if (handCard == null) handCard = buildHandCard(rs);
			
			hand.add(handCard);
			
		}
		
		rs.close();
		
		return hand;
		
	}
	
	public static Hand getFromIdentityMap(long id) {
		
		Hand handCard = null;
		
		try {
			handCard = IdentityMap.get(id, Hand.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return handCard;
		
	}

}
