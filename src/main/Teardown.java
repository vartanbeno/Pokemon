package main;
import app.FrontController;

public class Teardown {
	
	public static void main(String[] args) {
		
		try {
			
			FrontController.initDb("");
			FrontController.dropTables();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			FrontController.closeDb();
		}
		
	}

}
