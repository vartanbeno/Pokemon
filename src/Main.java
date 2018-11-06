import app.PageController;
import dom.model.card.rdg.CardRDG;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.deck.rdg.DeckRDG;
import dom.model.game.rdg.GameRDG;
import dom.model.user.rdg.UserRDG;

public class Main {

	public static void main(String[] args) {
				
		try {
			
			PageController.initDb("");
			
			UserRDG.createTable();
			ChallengeRDG.createTable();
			DeckRDG.createTable();
			CardRDG.createTable();
			GameRDG.createTable();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			PageController.closeDb();
		}
		
	}

}
