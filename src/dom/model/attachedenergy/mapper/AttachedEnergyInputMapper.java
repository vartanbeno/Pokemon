package dom.model.attachedenergy.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.attachedenergy.AttachedEnergy;
import dom.model.attachedenergy.AttachedEnergyFactory;
import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.attachedenergy.tdg.AttachedEnergyFinder;
import dom.model.card.ICard;
import dom.model.card.mapper.CardInputMapper;
import dom.model.game.Game;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class AttachedEnergyInputMapper {
	
	public static AttachedEnergy findById(long id) throws SQLException {
		
		AttachedEnergy attachedEnergy = getFromIdentityMap(id);
		if (attachedEnergy != null) return attachedEnergy;
		
		ResultSet rs = AttachedEnergyFinder.findById(id);
		
		attachedEnergy = rs.next() ? buildAttachedEnergy(rs) : null;
		rs.close();
		
		return attachedEnergy;
		
	}
	
	public static List<IAttachedEnergy> findByGame(long game) throws SQLException {
		
		ResultSet rs = AttachedEnergyFinder.findByGame(game);
		
		List<IAttachedEnergy> attachedEnergies = buildAttachedEnergies(rs);
		rs.close();
		
		return attachedEnergies;
		
	}
	
	public static List<IAttachedEnergy> findByGameAndPlayer(long game, long player) throws SQLException {
		
		ResultSet rs = AttachedEnergyFinder.findByGameAndPlayer(game, player);
		
		List<IAttachedEnergy> attachedEnergies = buildAttachedEnergies(rs);
		rs.close();
		
		return attachedEnergies;
		
	}
	
	public static List<IAttachedEnergy> findByGameAndPlayerAndPokemonCard(long game, long player, long card) throws SQLException {
		
		ResultSet rs = AttachedEnergyFinder.findByGameAndPlayerAndPokemonCard(game, player, card);
		
		List<IAttachedEnergy> attachedEnergies = buildAttachedEnergies(rs);
		rs.close();
		
		return attachedEnergies;
		
	}
	
	public static List<IAttachedEnergy> findByGameAndGameTurnAndPlayer(long game, long gameTurn, long player) throws SQLException {
		
		ResultSet rs = AttachedEnergyFinder.findByGameAndGameTurnAndPlayer(game, gameTurn, player);
		
		List<IAttachedEnergy> attachedEnergies = buildAttachedEnergies(rs);
		rs.close();
		
		return attachedEnergies;
		
	}
	
	public static AttachedEnergy buildAttachedEnergy(ResultSet rs) throws SQLException {
		
		Game game = GameInputMapper.findById(rs.getLong("game"));
		User player = UserInputMapper.findById(rs.getLong("player"));
		ICard energyCard = CardInputMapper.findById(rs.getLong("energy_card"));
		
		return AttachedEnergyFactory.createClean(
				rs.getLong("id"), rs.getLong("version"),
				game, rs.getLong("game_turn"), player,
				energyCard, rs.getLong("pokemon_card")
		);
		
	}
	
	public static List<IAttachedEnergy> buildAttachedEnergies(ResultSet rs) throws SQLException {
		
		List<IAttachedEnergy> attachedEnergies = new ArrayList<IAttachedEnergy>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			AttachedEnergy attachedEnergy = getFromIdentityMap(id);
			
			if (attachedEnergy == null) attachedEnergy = buildAttachedEnergy(rs);
			
			attachedEnergies.add(attachedEnergy);
			
		}
		
		rs.close();
		
		return attachedEnergies;
		
	}
	
	public static AttachedEnergy getFromIdentityMap(long id) {
		
		AttachedEnergy attachedEnergy = null;
		
		try {
			attachedEnergy = IdentityMap.get(id, AttachedEnergy.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return attachedEnergy;
		
	}

}
