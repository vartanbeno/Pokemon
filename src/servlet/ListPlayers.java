package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.threadLocal.DbRegistry;

import dom.model.user.rdg.UserRDG;

@WebServlet("/ListPlayers")
public class ListPlayers extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(javax.servlet.ServletConfig config) throws ServletException {
		try {
			String key = "";
			MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory(null, null, null, null);
			connectionFactory.defaultInitialization();
			DbRegistry.setConFactory(key, connectionFactory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public ListPlayers() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			List<UserRDG> userRDGs = UserRDG.findAll();
			
			request.setAttribute("players", userRDGs);
			request.getRequestDispatcher(Global.LIST_PLAYERS).forward(request, response);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
