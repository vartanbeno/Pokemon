package dom.model.cardinplay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;

import dom.model.card.Card;
import dom.model.card.mapper.CardMapper;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.tdg.CardInPlayTDG;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.game.Game;
import dom.model.game.mapper.GameMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserMapper;

public class CardInPlayMapper extends GenericOutputMapper<Long, CardInPlay> {

	@Override
	public void insert(CardInPlay cardInPlay) throws MapperException {
		try {
			insertStatic(cardInPlay);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void update(CardInPlay cardInPlay) throws MapperException {
		try {
			updateStatic(cardInPlay);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	@Override
	public void delete(CardInPlay cardInPlay) throws MapperException {
		try {
			deleteStatic(cardInPlay);
		}
		catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	
	public static void insertStatic(CardInPlay cardInPlay) throws SQLException {
		CardInPlayTDG.insert(
				cardInPlay.getId(),
				cardInPlay.getVersion(),
				cardInPlay.getGame().getId(),
				cardInPlay.getPlayer().getId(),
				cardInPlay.getDeck().getId(),
				cardInPlay.getCard().getId()
		);
	}
	
	public static void updateStatic(CardInPlay cardInPlay) throws SQLException {
		CardInPlayTDG.update(cardInPlay.getStatus(), cardInPlay.getId(), cardInPlay.getVersion());
	}
	
	public static void deleteStatic(CardInPlay cardInPlay) throws SQLException {
		CardInPlayTDG.delete(cardInPlay.getId(), cardInPlay.getVersion());
	}
	
	public static List<ICardInPlay> findAll() throws SQLException {
		
		ResultSet rs = CardInPlayTDG.findAll();
		List<ICardInPlay> cardsInPlay = buildCardsInPlay(rs);
		rs.close();
		
		return cardsInPlay;
		
	}
	
	public static CardInPlay findById(long id) throws SQLException {
		
		ResultSet rs = CardInPlayTDG.findById(id);
		CardInPlay cardInPlay = rs.next() ? buildCardInPlay(rs) : null;
		rs.close();
		
		return cardInPlay;
		
	}
	
	public static CardInPlay findByCard(long card) throws SQLException {
		
		ResultSet rs = CardInPlayTDG.findByCard(card);
		CardInPlay cardInPlay = rs.next() ? buildCardInPlay(rs) : null;
		rs.close();
		
		return cardInPlay;
		
	}
	
	public static List<ICardInPlay> findByGameAndPlayer(long game, long player) throws SQLException {
		
		ResultSet rs = CardInPlayTDG.findByGameAndPlayer(game, player);
		List<ICardInPlay> cardsInPlay = buildCardsInPlay(rs);
		rs.close();
		
		return cardsInPlay;
		
	}
	
	public static List<ICardInPlay> findByGameAndPlayerAndStatus(long game, long player, int status) throws SQLException {
		
		ResultSet rs = CardInPlayTDG.findByGameAndPlayerAndStatus(game, player, status);
		List<ICardInPlay> cardsInPlay = buildCardsInPlay(rs);
		rs.close();
		
		return cardsInPlay;
		
	}
	
	public static CardInPlay buildCardInPlay(ResultSet rs) throws SQLException {
		
		Game game = GameMapper.findById(rs.getLong("game"));
		User player = UserMapper.findById(rs.getLong("player"));
		Deck deck = DeckMapper.findById(rs.getLong("deck"));
		Card card = CardMapper.findById(rs.getLong("card"));
		
		return new CardInPlay(rs.getLong("id"), rs.getLong("version"), game, player, deck, card, rs.getInt("status"));
		
	}
	
	public static List<ICardInPlay> buildCardsInPlay(ResultSet rs) throws SQLException {
		
		List<ICardInPlay> cardsInPlay = new ArrayList<ICardInPlay>();
		
		while (rs.next()) {
			
			CardInPlay cardInPlay = buildCardInPlay(rs);
			cardsInPlay.add(cardInPlay);
			
		}
		
		rs.close();
		
		return cardsInPlay;
		
	}

}
