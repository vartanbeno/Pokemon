package dom.model.discard.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.card.CardProxy;
import dom.model.discard.Discard;
import dom.model.discard.DiscardFactory;
import dom.model.discard.IDiscard;
import dom.model.discard.tdg.DiscardFinder;
import dom.model.game.GameProxy;
import dom.model.user.UserProxy;

public class DiscardInputMapper {
	
	public static List<IDiscard> findAll() throws SQLException {
		
		ResultSet rs = DiscardFinder.findAll();
		
		List<IDiscard> discardPile = buildDiscardPile(rs);
		rs.close();
		
		return discardPile;
		
	}
	
	public static Discard findById(long id) throws SQLException {
		
		Discard discardCard = getFromIdentityMap(id);
		if (discardCard != null) return discardCard;
		
		ResultSet rs = DiscardFinder.findById(id);
		
		discardCard = rs.next() ? buildDiscardCard(rs) : null;
		rs.close();
		
		return discardCard;
		
	}
	
	public static List<IDiscard> findByGameAndPlayer(long game, long player) throws SQLException {
		
		ResultSet rs = DiscardFinder.findByGameAndPlayer(game, player);
		List<IDiscard> discardPile = buildDiscardPile(rs);
		rs.close();
		
		return discardPile;
		
	}
	
	public static Discard buildDiscardCard(ResultSet rs) throws SQLException {
		
		return DiscardFactory.createClean(
				rs.getLong("id"),
				rs.getLong("version"),
				new GameProxy(rs.getLong("game")),
				new UserProxy(rs.getLong("player")),
				rs.getLong("deck"),
				new CardProxy(rs.getLong("card"))
		);
		
	}
	
	public static List<IDiscard> buildDiscardPile(ResultSet rs) throws SQLException {
		
		List<IDiscard> discardPile = new ArrayList<IDiscard>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			Discard discardCard = getFromIdentityMap(id);
			
			if (discardCard == null) discardCard = buildDiscardCard(rs);
			
			discardPile.add(discardCard);
			
		}
		
		rs.close();
		
		return discardPile;
		
	}
	
	public static Discard getFromIdentityMap(long id) {
		
		Discard discardCard = null;
		
		try {
			discardCard = IdentityMap.get(id, Discard.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return discardCard;
		
	}

}
