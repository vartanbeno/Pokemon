import dom.model.card.rdg.CardRDG;
import dom.model.cardsindeck.rdg.CardsInDeckRDG;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.deck.rdg.DeckRDG;
import dom.model.user.rdg.UserRDG;
import servlet.PageController;

public class Main {

	public static void main(String[] args) {
				
		try {
			
			PageController.initDb();
			
			UserRDG.createTable();
			ChallengeRDG.createTable();
			CardRDG.createTable();
			DeckRDG.createTable();
			CardsInDeckRDG.createTable();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			PageController.closeDb();
		}
		
	}

}
