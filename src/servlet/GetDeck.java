package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dom.model.card.rdg.CardRDG;

@WebServlet("/GetDeck")
public class GetDeck extends PageController {

	private static final long serialVersionUID = 1L;
       
    public GetDeck() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			if (loggedIn(request, response)) {
								
				List<CardRDG> cards = CardRDG.findAll();
				List<CardRDG> deck = new ArrayList<CardRDG>();
				
				Random random = new Random();
				for (int i = 0; i < 40; i++) {
					CardRDG card = cards.get(random.nextInt(cards.size()));
					deck.add(card);
				}
				
				request.setAttribute("cards", deck);
				request.getRequestDispatcher(Global.DECK).forward(request, response);
				
			}
			else {
				failure(request, response, "You must be logged in to get a deck.");
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
		doGet(request, response);
	}

}
