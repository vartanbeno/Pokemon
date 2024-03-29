package dom.model.challenge;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.challenge.tdg.ChallengeTDG;
import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class ChallengeFactory {
	
	public static Challenge createNew(IUser challenger, IUser challengee, IDeck challengerDeck)
			throws MissingMappingException, MapperException, SQLException {
		
		Challenge challenge = new Challenge(
				ChallengeTDG.getMaxId(), 1, challenger, challengee, ChallengeStatus.open.ordinal(), challengerDeck
		);
		UoW.getCurrent().registerNew(challenge);
		
		return challenge;
		
	}
	
	public static Challenge createClean(long id, long version, IUser challenger, IUser challengee, int status, IDeck challengerDeck) {
		
		Challenge challenge = new Challenge(id, version, challenger, challengee, status, challengerDeck);
		UoW.getCurrent().registerClean(challenge);
		
		return challenge;
		
	}
	
	public static Challenge registerDirty(long id, long version, IUser challenger, IUser challengee, int status, IDeck challengerDeck)
			throws MissingMappingException, MapperException {
		
		Challenge challenge = new Challenge(id, version, challenger, challengee, status, challengerDeck);
		UoW.getCurrent().registerDirty(challenge);
		
		return challenge;
		
	}

}
