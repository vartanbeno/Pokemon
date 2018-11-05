package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.rdg.ChallengeRDG;

@WebServlet("/ListChallenges")
public class ListChallenges extends PageController {
	
	private static final long serialVersionUID = 1L;

    public ListChallenges() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			List<ChallengeRDG> challengeRDGs = ChallengeRDG.findAll();
			request.setAttribute("challenges", challengeRDGs);
			request.getRequestDispatcher(Global.LIST_CHALLENGES).forward(request, response);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
