package main;
import app.PageController;

public class Teardown {
	
	public static void main(String[] args) {
		
		try {
			
			PageController.initDb("");
			PageController.dropTables();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			PageController.closeDb();
		}
		
	}

}
