package dom.model.cardinplay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.card.Card;
import dom.model.card.mapper.CardInputMapper;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.tdg.CardInPlayFinder;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckInputMapper;
import dom.model.game.Game;
import dom.model.game.mapper.GameInputMapper;
import dom.model.user.User;
import dom.model.user.mapper.UserInputMapper;

public class CardInPlayInputMapper {
	
	public static List<ICardInPlay> findAll() throws SQLException {
		
		ResultSet rs = CardInPlayFinder.findAll();
		List<ICardInPlay> cardsInPlay = buildCardsInPlay(rs);
		rs.close();
		
		return cardsInPlay;
		
	}
	
	public static CardInPlay findById(long id) throws SQLException {
		
		ResultSet rs = CardInPlayFinder.findById(id);
		CardInPlay cardInPlay = rs.next() ? buildCardInPlay(rs) : null;
		rs.close();
		
		return cardInPlay;
		
	}
	
	public static CardInPlay findByCard(long card) throws SQLException {
		
		ResultSet rs = CardInPlayFinder.findByCard(card);
		CardInPlay cardInPlay = rs.next() ? buildCardInPlay(rs) : null;
		rs.close();
		
		return cardInPlay;
		
	}
	
	public static List<ICardInPlay> findByGameAndPlayer(long game, long player) throws SQLException {
		
		ResultSet rs = CardInPlayFinder.findByGameAndPlayer(game, player);
		List<ICardInPlay> cardsInPlay = buildCardsInPlay(rs);
		rs.close();
		
		return cardsInPlay;
		
	}
	
	public static List<ICardInPlay> findByGameAndPlayerAndStatus(long game, long player, int status) throws SQLException {
		
		ResultSet rs = CardInPlayFinder.findByGameAndPlayerAndStatus(game, player, status);
		List<ICardInPlay> cardsInPlay = buildCardsInPlay(rs);
		rs.close();
		
		return cardsInPlay;
		
	}
	
	public static CardInPlay buildCardInPlay(ResultSet rs) throws SQLException {
		
		Game game = GameInputMapper.findById(rs.getLong("game"));
		User player = UserInputMapper.findById(rs.getLong("player"));
		Deck deck = DeckInputMapper.findById(rs.getLong("deck"));
		Card card = CardInputMapper.findById(rs.getLong("card"));
		
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