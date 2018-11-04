package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.challenge.rdg.ChallengeRDG;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ChallengePlayer")
public class ChallengePlayer extends PageController {
	
	private static final long serialVersionUID = 1L;

    public ChallengePlayer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			try {
				final long challenger = (long) request.getSession(true).getAttribute("userid");
				
				List<UserRDG> userRDGs = UserRDG.findAll();
				userRDGs.removeIf(userRDG -> userRDG.getId() == challenger);
				
				request.setAttribute("users", userRDGs);
				request.getRequestDispatcher(Global.CHALLENGE_FORM).forward(request, response);
			}
			catch (NullPointerException e) {
				response.sendRedirect(request.getContextPath() + "/Login");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
									
			long challenger = (long) request.getSession(true).getAttribute("userid");
			long challengee = Long.parseLong(request.getParameter("challengee"));
			UserRDG userRDG = UserRDG.findById(challengee);
			
			if (challenger == challengee) {
				failure(request, response, "You have challenged yourself. You are not allowed to do that");
			}
			else {
				ChallengeRDG challengeRDG = new ChallengeRDG(ChallengeRDG.getMaxId(), challenger, challengee);
				challengeRDG.insert();
				success(request, response, String.format("You have successfuly challenged %s to a match.", userRDG.getUsername()));
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeDb();
		}
		
	}

}
