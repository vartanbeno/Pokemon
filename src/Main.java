import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.user.rdg.UserRDG;

public class Main {

	public static void main(String[] args) {
				
		try {
			
			String key = "";
			MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory(null, null, null, null);
			connectionFactory.defaultInitialization();
			DbRegistry.setConFactory(key, connectionFactory);
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			UserRDG.createTable();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}

}
