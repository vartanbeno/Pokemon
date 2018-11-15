package main;
import app.PageController;

public class Setup {

	public static void main(String[] args) {
				
		try {
			
			PageController.initDb("");
			PageController.createTables();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			PageController.closeDb();
		}
		
	}

}
