package dom.model.hand.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.card.CardProxy;
import dom.model.game.GameProxy;
import dom.model.hand.Hand;
import dom.model.hand.HandFactory;
import dom.model.hand.IHand;
import dom.model.hand.tdg.HandFinder;
import dom.model.user.UserProxy;

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
	
	public static Hand findByGameAndPlayerAndCard(long game, long player, long card) throws SQLException {
		
		Hand handCard = null;
		
		ResultSet rs = HandFinder.findByGameAndPlayerAndCard(game, player, card);
		
		if (rs.next()) {
			
			long id = rs.getLong("id");
			
			handCard = getFromIdentityMap(id);
			if (handCard == null) handCard = buildHandCard(rs);
			
			rs.close();
			
		}
		
		return handCard;
		
	}
	
	public static Hand buildHandCard(ResultSet rs) throws SQLException {
		
		return HandFactory.createClean(
				rs.getLong("id"),
				rs.getLong("version"),
				new GameProxy(rs.getLong("game")),
				new UserProxy(rs.getLong("player")),
				rs.getLong("deck"),
				new CardProxy(rs.getLong("card"))
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
