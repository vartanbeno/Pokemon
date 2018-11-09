package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.CardHelper;
import dom.model.card.rdg.CardRDG;
import dom.model.cardinplay.CardInPlayHelper;
import dom.model.cardinplay.CardStatus;
import dom.model.cardinplay.rdg.CardInPlayRDG;
import dom.model.deck.DeckHelper;
import dom.model.deck.DeckWithCardsHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.GameBoardHelper;
import dom.model.game.GameHelper;
import dom.model.game.rdg.GameRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ViewBoard")
public class ViewBoard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view a game board.";
	
    public ViewBoard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (!loggedIn(request)) {
				failure(request, response, NOT_LOGGED_IN);
				return;
			}
			
			GameRDG gameRDG = getGame(request, response);
			if (gameRDG == null) return;
			
			UserRDG challengerRDG = UserRDG.findById(gameRDG.getChallenger());
			UserRDG challengeeRDG = UserRDG.findById(gameRDG.getChallengee());
			
			DeckRDG challengerDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallenger());
			DeckRDG challengeeDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallengee());
			
			List<CardRDG> challengerDeckCardsRDG = CardRDG.findByDeck(gameRDG.getChallengerDeck());
			List<CardRDG> challengeeDeckCardsRDG = CardRDG.findByDeck(gameRDG.getChallengeeDeck());
			
			UserHelper challenger = new UserHelper(
					challengerRDG.getId(),
					challengerRDG.getVersion(),
					challengerRDG.getUsername(),
					""
			);
			
			UserHelper challengee = new UserHelper(
					challengeeRDG.getId(),
					challengeeRDG.getVersion(),
					challengeeRDG.getUsername(),
					""
			);
			
			DeckHelper challengerDeck = new DeckHelper(
					challengerDeckRDG.getId(),
					challenger
			);
			
			DeckHelper challengeeDeck = new DeckHelper(
					challengeeDeckRDG.getId(),
					challengee
			);
			
			GameHelper game = new GameHelper(
					gameRDG.getId(),
					challenger,
					challengee,
					challengerDeck,
					challengeeDeck,
					gameRDG.getStatus()
			);
			
			List<CardHelper> challengerCards = new ArrayList<CardHelper>();
			List<CardHelper> challengeeCards = new ArrayList<CardHelper>();
			
			/**
			 * Generate list of card helpers for both decks.
			 * i.e. 40 cards each.
			 */
			
			for (CardRDG cardRDG : challengerDeckCardsRDG) {
				
				CardRDG challengerCardRDG = CardRDG.findById(cardRDG.getId());
				CardHelper challengerCard = new CardHelper(
						challengerCardRDG.getId(),
						challengerDeck,
						challengerCardRDG.getType(),
						challengerCardRDG.getName()
				);
				
				challengerCards.add(challengerCard);
				
			}
			
			for (CardRDG cardRDG : challengeeDeckCardsRDG) {
				
				CardRDG challengeeCardRDG = CardRDG.findById(cardRDG.getId());
				CardHelper challengeeCard = new CardHelper(
						challengeeCardRDG.getId(),
						challengeeDeck,
						challengeeCardRDG.getType(),
						challengeeCardRDG.getName()
				);
				
				challengeeCards.add(challengeeCard);
				
			}
			
			/**
			 * These are all cards of each player that are have been drawn from the decks,
			 * regardless of their status (i.e. in hand, benched, discarded).
			 * We will split them based on status when we initiate the CardInPlayerHelper objects.
			 */
			List<CardInPlayRDG> challengerCardInPlayRDGs = CardInPlayRDG.findByGameAndPlayer(
					gameRDG.getId(), gameRDG.getChallenger()
			);
			List<CardInPlayRDG> challengeeCardInPlayRDGs = CardInPlayRDG.findByGameAndPlayer(
					gameRDG.getId(), gameRDG.getChallengee()
			);
			
			/**
			 * These are all CardInPlay helper objects, taken from the CardInPlay RDG data above.
			 * We will split them after filling up the lists.
			 */
			List<CardInPlayHelper> challengerCardsInPlay = new ArrayList<CardInPlayHelper>();
			List<CardInPlayHelper> challengeeCardsInPlay = new ArrayList<CardInPlayHelper>();
			
			for (CardInPlayRDG challengerCardInPlayRDG : challengerCardInPlayRDGs) {
				
				CardRDG challengerCardRDG = CardRDG.findById(challengerCardInPlayRDG.getCard());
				CardHelper challengerCard = new CardHelper(
						challengerCardRDG.getId(),
						challengerDeck,
						challengerCardRDG.getType(),
						challengerCardRDG.getName()
				);
				
				CardInPlayHelper challengerCardInPlay = new CardInPlayHelper(
						challengerCardInPlayRDG.getId(),
						game,
						challenger,
						challengerDeck,
						challengerCard,
						challengerCardInPlayRDG.getStatus()
				);
				
				challengerCardsInPlay.add(challengerCardInPlay);
				
			}
			
			for (CardInPlayRDG challengeeCardInPlayRDG : challengeeCardInPlayRDGs) {
				
				CardRDG challengeeCardRDG = CardRDG.findById(challengeeCardInPlayRDG.getCard());
				CardHelper challengeeCard = new CardHelper(
						challengeeCardRDG.getId(),
						challengeeDeck,
						challengeeCardRDG.getType(),
						challengeeCardRDG.getName()
				);
				
				CardInPlayHelper challengeeCardInPlay = new CardInPlayHelper(
						challengeeCardInPlayRDG.getId(),
						game,
						challengee,
						challengeeDeck,
						challengeeCard,
						challengeeCardInPlayRDG.getStatus()
				);
				
				challengeeCardsInPlay.add(challengeeCardInPlay);
				
			}
			
			/**
			 * We make sure to remove X number of cards from the 40-card deck.
			 * Decks are always so it's just a matter of removing the first one X times.
			 * i.e. if we have 13 cards in play, we remove 13 cards from the top.
			 * So now we have 27 cards left.
			 */
			IntStream.range(0, challengerCardsInPlay.size()).forEach($ -> challengerCards.remove(0));
			IntStream.range(0, challengeeCardsInPlay.size()).forEach($ -> challengeeCards.remove(0));
			
			/**
			 * Now let's create separate lists per card status:
			 *  - One for in hard.
			 *  - One for benched.
			 *  - One for discarded.
			 *  
			 * We do this for each player.
			 */
			List<CardInPlayHelper> challengerHandCards =
					challengerCardsInPlay.stream().filter(
							card -> card.getStatus() == CardStatus.hand.ordinal()
					).collect(Collectors.toList());
			
			List<CardInPlayHelper> challengeeHandCards =
					challengeeCardsInPlay.stream().filter(
							card -> card.getStatus() == CardStatus.hand.ordinal()
					).collect(Collectors.toList());
			
			List<CardInPlayHelper> challengerBenchedCards =
					challengerCardsInPlay.stream().filter(
							card -> card.getStatus() == CardStatus.benched.ordinal()
					).collect(Collectors.toList());
			
			List<CardInPlayHelper> challengeeBenchedCards =
					challengeeCardsInPlay.stream().filter(
							card -> card.getStatus() == CardStatus.benched.ordinal()
					).collect(Collectors.toList());
			
			List<CardInPlayHelper> challengerDiscardedCards =
					challengerCardsInPlay.stream().filter(
							card -> card.getStatus() == CardStatus.discarded.ordinal()
					).collect(Collectors.toList());
			
			List<CardInPlayHelper> challengeeDiscardedCards =
					challengeeCardsInPlay.stream().filter(
							card -> card.getStatus() == CardStatus.discarded.ordinal()
					).collect(Collectors.toList());
			
			DeckWithCardsHelper challengerDeckWithCards = new DeckWithCardsHelper(
					gameRDG.getChallengerDeck(),
					challenger,
					challengerCards
			);
			
			DeckWithCardsHelper challengeeDeckWithCards = new DeckWithCardsHelper(
					gameRDG.getChallengeeDeck(),
					challengee,
					challengeeCards
			);
			
			GameBoardHelper gameBoard = new GameBoardHelper(
					gameRDG.getId(),
					challenger, challengee,
					challengerDeckWithCards, challengeeDeckWithCards,
					challengerHandCards, challengeeHandCards,
					challengerBenchedCards, challengeeBenchedCards,
					challengerDiscardedCards, challengeeDiscardedCards
			);
			
			request.setAttribute("game", gameBoard);
			request.getRequestDispatcher(Global.VIEW_BOARD).forward(request, response);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);		
	}

}
