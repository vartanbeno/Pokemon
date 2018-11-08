package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.CardHelper;
import dom.model.card.rdg.CardRDG;
import dom.model.deck.DeckHelper;
import dom.model.deck.DeckWithCardsHelper;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.GameBoardHelper;
import dom.model.game.GameHelper;
import dom.model.game.rdg.GameRDG;
import dom.model.hand.HandHelper;
import dom.model.hand.rdg.HandRDG;
import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ViewBoard")
public class ViewBoard extends PageController {
	
	private static final long serialVersionUID = 1L;
	
	private static final String NOT_LOGGED_IN = "You must be logged in to view a game board.";
	private static final String WRONG_FORMAT = "You must specify a game ID in the correct format (an integer).";
	private static final String INVALID_GAME = "The game you specified does not exist.";
	private static final String NOT_YOUR_GAME = "You are not part of this game.";
	
    public ViewBoard() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request)) {
				
				Long gameId = null;
				
				try {
					gameId = Long.parseLong(request.getParameter("game"));
				}
				catch (NumberFormatException e) {
					failure(request, response, WRONG_FORMAT);
					return;
				}
				
				GameRDG gameRDG = GameRDG.findById(gameId);
				
				if (gameRDG != null) {
					
					long userId = getUserId(request);
					
					if (userId != gameRDG.getChallenger() && userId != gameRDG.getChallengee()) {
						failure(request, response, NOT_YOUR_GAME);
						return;
					}
					
					UserRDG challengerRDG = UserRDG.findById(gameRDG.getChallenger());
					UserRDG challengeeRDG = UserRDG.findById(gameRDG.getChallengee());
					
					DeckRDG challengerDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallenger());
					DeckRDG challengeeDeckRDG = DeckRDG.findByPlayer(gameRDG.getChallengee());
					
					List<CardRDG> challengerDeckCardsRDG = CardRDG.findByDeck(gameRDG.getChallengerDeck());
					List<CardRDG> challengeeDeckCardsRDG = CardRDG.findByDeck(gameRDG.getChallengeeDeck());
					
					List<HandRDG> challengerHandRDG = HandRDG.findByGameAndPlayer(gameRDG.getId(), gameRDG.getChallenger());
					List<HandRDG> challengeeHandRDG = HandRDG.findByGameAndPlayer(gameRDG.getId(), gameRDG.getChallengee());
					
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
							challengeeDeck
					);
					
					CardRDG challengerCardRDG = null;
					CardRDG challengeeCardRDG = null;
					
					CardHelper challengerCard = null;
					CardHelper challengeeCard = null;
					List<CardHelper> challengerCards = new ArrayList<CardHelper>();
					List<CardHelper> challengeeCards = new ArrayList<CardHelper>();
					
					for (CardRDG cardRDG : challengerDeckCardsRDG) {
						
						challengerCardRDG = CardRDG.findById(cardRDG.getId());
						challengerCard = new CardHelper(
								challengerCardRDG.getId(),
								challengerDeck,
								challengerCardRDG.getType(),
								challengerCardRDG.getName()
						);
						
						challengerCards.add(challengerCard);
						
					}
					
					for (CardRDG cardRDG : challengeeDeckCardsRDG) {
						
						challengeeCardRDG = CardRDG.findById(cardRDG.getId());
						challengeeCard = new CardHelper(
								challengeeCardRDG.getId(),
								challengeeDeck,
								challengeeCardRDG.getType(),
								challengeeCardRDG.getName()
						);
						
						challengeeCards.add(challengeeCard);
						
					}
					
					HandHelper challengerCardInHand = null;
					HandHelper challengeeCardInHand = null;
					List<HandHelper> challengerHand = new ArrayList<HandHelper>();
					List<HandHelper> challengeeHand = new ArrayList<HandHelper>();
					
					for (HandRDG handRDG : challengerHandRDG) {
						
						challengerCardRDG = CardRDG.findById(handRDG.getCard());
						challengerCard = new CardHelper(
								challengerCardRDG.getId(),
								challengerDeck,
								challengerCardRDG.getType(),
								challengerCardRDG.getName()
						);
						
						challengerCardInHand = new HandHelper(
								handRDG.getId(),
								game,
								challenger,
								challengerDeck,
								challengerCard
						);
						
						challengerHand.add(challengerCardInHand);
						
					}
					
					for (HandRDG handRDG : challengeeHandRDG) {
						
						challengeeCardRDG = CardRDG.findById(handRDG.getCard());
						challengeeCard = new CardHelper(
								challengeeCardRDG.getId(),
								challengeeDeck,
								challengeeCardRDG.getType(),
								challengeeCardRDG.getName()
						);
						
						challengeeCardInHand = new HandHelper(
								handRDG.getId(),
								game,
								challengee,
								challengeeDeck,
								challengeeCard
						);
						
						challengeeHand.add(challengeeCardInHand);
						
					}
					
					IntStream.range(0, challengerHand.size()).forEach($ -> challengerCards.remove(0));
					IntStream.range(0, challengeeHand.size()).forEach($ -> challengeeCards.remove(0));
					
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
							challengerHand, challengeeHand
					);
					
					request.setAttribute("game", gameBoard);
					request.getRequestDispatcher(Global.VIEW_BOARD).forward(request, response);
					
				}
				else {
					failure(request, response, INVALID_GAME);
				}
				
			}
			else {
				failure(request, response, NOT_LOGGED_IN);
			}
			
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
