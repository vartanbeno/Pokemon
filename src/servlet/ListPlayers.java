package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.user.UserHelper;
import dom.model.user.rdg.UserRDG;

@WebServlet("/ListPlayers")
public class ListPlayers extends PageController {
	
	private static final long serialVersionUID = 1L;
	
    public ListPlayers() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
						
			List<UserRDG> userRDGs = UserRDG.findAll();
			
			List<UserHelper> users = new ArrayList<UserHelper>();
			for (UserRDG userRDG : userRDGs) {
				users.add(
					new UserHelper(userRDG.getId(), userRDG.getVersion(), userRDG.getUsername(), "")
				);
			}
			
			request.setAttribute("players", users);
			request.getRequestDispatcher(Global.LIST_PLAYERS).forward(request, response);
			
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
