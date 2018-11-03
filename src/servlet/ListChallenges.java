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

import dom.model.challenge.rdg.ChallengeRDG;

@WebServlet("/ListChallenges")
public class ListChallenges extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LIST_CHALLENGES_JSP = "/WEB-INF/jsp/list-challenges.jsp";

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

    public ListChallenges() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			
			List<ChallengeRDG> challengeRDGs = ChallengeRDG.findAll();
			
			request.setAttribute("challenges", challengeRDGs);
			request.getRequestDispatcher(LIST_CHALLENGES_JSP).forward(request, response);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
