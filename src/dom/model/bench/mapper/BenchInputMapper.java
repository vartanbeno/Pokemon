package dom.model.bench.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.ObjectRemovedException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;

import dom.model.attachedenergy.IAttachedEnergy;
import dom.model.attachedenergy.mapper.AttachedEnergyInputMapper;
import dom.model.bench.Bench;
import dom.model.bench.BenchFactory;
import dom.model.bench.IBench;
import dom.model.bench.tdg.BenchFinder;
import dom.model.card.Card;
import dom.model.card.mapper.CardInputMapper;
import dom.model.game.Game;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class BenchInputMapper {
	
	public static List<IBench> findAll() throws SQLException {
		
		ResultSet rs = BenchFinder.findAll();
		
		List<IBench> bench = buildBench(rs);
		rs.close();
		
		return bench;
		
	}
	
	public static Bench findById(long id) throws SQLException {
		
		Bench benchCard = getFromIdentityMap(id);
		if (benchCard != null) return benchCard;
		
		ResultSet rs = BenchFinder.findById(id);
		
		benchCard = rs.next() ? buildBenchCard(rs) : null;
		rs.close();
		
		return benchCard;
		
	}
	
	public static List<IBench> findByGameAndPlayer(long game, long player) throws SQLException {
		
		ResultSet rs = BenchFinder.findByGameAndPlayer(game, player);
		List<IBench> bench = buildBench(rs);
		rs.close();
		
		return bench;
		
	}
	
	public static Bench buildBenchCard(ResultSet rs) throws SQLException {
		
		Game game = GameInputMapper.findById(rs.getLong("game"));
		User player = UserInputMapper.findById(rs.getLong("player"));
		Card card = CardInputMapper.findById(rs.getLong("card"));
		List<IAttachedEnergy> attachedEnergyCards = AttachedEnergyInputMapper.findByGameAndPlayer(rs.getLong("game"), rs.getLong("player"));
		
		return BenchFactory.createClean(
				rs.getLong("id"),
				rs.getLong("version"),
				game,
				player,
				rs.getLong("deck"),
				card,
				attachedEnergyCards
		);
		
	}
	
	public static List<IBench> buildBench(ResultSet rs) throws SQLException {
		
		List<IBench> bench = new ArrayList<IBench>();
		
		while (rs.next()) {
			
			long id = rs.getLong("id");
			Bench benchCard = getFromIdentityMap(id);
			
			if (benchCard == null) benchCard = buildBenchCard(rs);
			
			bench.add(benchCard);
			
		}
		
		rs.close();
		
		return bench;
		
	}
	
	public static Bench getFromIdentityMap(long id) {
		
		Bench benchCard = null;
		
		try {
			benchCard = IdentityMap.get(id, Bench.class);
		}
		catch (DomainObjectNotFoundException | ObjectRemovedException e) { }
		
		return benchCard;
		
	}

}
