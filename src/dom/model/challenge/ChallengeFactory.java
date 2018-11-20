package dom.model.challenge;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.MissingMappingException;
import org.dsrg.soenea.uow.UoW;

import dom.model.challenge.tdg.ChallengeTDG;
import dom.model.deck.IDeck;
import dom.model.user.IUser;

public class ChallengeFactory {
	
	public static Challenge createNew(long id, long version, IUser challenger, IUser challengee, int status, IDeck challengerDeck)
			throws MissingMappingException, MapperException {
		
		Challenge challenge = new Challenge(id, version, challenger, challengee, status, challengerDeck);
		UoW.getCurrent().registerNew(challenge);
		
		return challenge;
		
	}
	
	public static Challenge createNew(IUser challenger, IUser challengee, IDeck challengerDeck)
			throws MissingMappingException, MapperException, SQLException {
		return createNew(
				ChallengeTDG.getMaxId(),
				1,
				challenger,
				challengee,
				ChallengeStatus.open.ordinal(),
				challengerDeck
		);
	}
	
	public static Challenge createNew(IChallenge challenge)
			throws MissingMappingException, MapperException {
		return createNew(
				challenge.getId(),
				challenge.getVersion(),
				challenge.getChallenger(),
				challenge.getChallengee(),
				challenge.getStatus(),
				challenge.getChallengerDeck()
		);
	}
	
	public static Challenge createClean(long id, long version, IUser challenger, IUser challengee, int status, IDeck challengerDeck) {
		
		Challenge challenge = new Challenge(id, version, challenger, challengee, status, challengerDeck);
		UoW.getCurrent().registerClean(challenge);
		
		return challenge;
		
	}
	
	public static Challenge createClean(IChallenge challenge) {
		return createClean(
				challenge.getId(),
				challenge.getVersion(),
				challenge.getChallenger(),
				challenge.getChallengee(),
				challenge.getStatus(),
				challenge.getChallengerDeck()
		);
	}
	
	public static Challenge registerDirty(long id, long version, IUser challenger, IUser challengee, int status, IDeck challengerDeck)
			throws MissingMappingException, MapperException {
		
		Challenge challenge = new Challenge(id, version, challenger, challengee, status, challengerDeck);
		UoW.getCurrent().registerDirty(challenge);
		
		return challenge;
		
	}
	
	public static Challenge registerDirty(IChallenge challenge)
			throws MissingMappingException, MapperException {
		return registerDirty(
				challenge.getId(),
				challenge.getVersion(),
				challenge.getChallenger(),
				challenge.getChallengee(),
				challenge.getStatus(),
				challenge.getChallengerDeck()
		);
	}

}
