import dom.model.card.rdg.CardRDG;
import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.rdg.UserRDG;
import servlet.PageController;

public class Main {

	public static void main(String[] args) {
				
		try {
			
			PageController.initDb();
						
			UserRDG.createTable();
			ChallengeRDG.createTable();
			CardRDG.createTable();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
