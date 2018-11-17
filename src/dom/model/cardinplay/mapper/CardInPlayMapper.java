package dom.model.cardinplay.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dom.model.card.Card;
import dom.model.card.mapper.CardMapper;
import dom.model.card.tdg.CardTDG;
import dom.model.cardinplay.CardInPlay;
import dom.model.cardinplay.ICardInPlay;
import dom.model.cardinplay.tdg.CardInPlayTDG;
import dom.model.deck.Deck;
import dom.model.deck.mapper.DeckMapper;
import dom.model.deck.tdg.DeckTDG;
import dom.model.game.Game;
import dom.model.game.mapper.GameMapper;
import dom.model.game.tdg.GameTDG;
import dom.model.user.User;
import dom.model.user.mapper.UserMapper;
import dom.model.user.tdg.UserTDG;

public class CardInPlayMapper {
	
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
	
	public static void insert(CardInPlay cardInPlay) throws SQLException {
		CardInPlayTDG.insert(
				cardInPlay.getId(),
				cardInPlay.getGame().getId(),
				cardInPlay.getPlayer().getId(),
				cardInPlay.getDeck().getId(),
				cardInPlay.getCard().getId()
		);
	}
	
	public static void update(CardInPlay cardInPlay) throws SQLException {
		CardInPlayTDG.update(cardInPlay.getStatus(), cardInPlay.getId());
	}
	
	public static void delete(CardInPlay cardInPlay) throws SQLException {
		CardInPlayTDG.delete(cardInPlay.getId());
	}
	
	public static CardInPlay buildCardInPlay(ResultSet rs) throws SQLException {
		
		ResultSet gameRS = GameTDG.findById(rs.getLong("game"));
		Game game = gameRS.next() ? GameMapper.buildGame(gameRS) : null;
		gameRS.close();
		
		ResultSet playerRS = UserTDG.findById(rs.getLong("player"));
		User player = playerRS.next() ? UserMapper.buildUser(playerRS) : null;
		playerRS.close();
		
		ResultSet deckRS = DeckTDG.findById(rs.getLong("deck"));
		Deck deck = deckRS.next() ? DeckMapper.buildDeck(deckRS) : null;
		deckRS.close();
		
		ResultSet cardRS = CardTDG.findById(rs.getLong("card"));
		Card card = cardRS.next() ? CardMapper.buildCard(cardRS) : null;
		cardRS.close();
		
		return new CardInPlay(rs.getLong("id"), game, player, deck, card, rs.getInt("status"));
		
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
