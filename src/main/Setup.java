package main;
import app.FrontController;

public class Setup {

	public static void main(String[] args) {
				
		try {
			
			FrontController.initDb("");
			FrontController.createTables();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			FrontController.closeDb();
		}
		
	}

}
