package dom.model.game;

import java.util.List;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.cardinplay.ICardInPlay;
import dom.model.deck.IDeck;
import dom.model.game.mapper.GameMapper;
import dom.model.user.IUser;

public class GameBoardProxy extends DomainObjectProxy<Long, GameBoard> implements IGameBoard {

	protected GameBoardProxy(Long id) {
		super(id);
	}

	@Override
	public IUser getChallenger() {
		return getInnerObject().getChallenger();
	}

	@Override
	public void setChallenger(IUser challenger) {
		getInnerObject().setChallenger(challenger);
	}

	@Override
	public IUser getChallengee() {
		return getInnerObject().getChallengee();
	}

	@Override
	public void setChallengee(IUser challengee) {
		getInnerObject().setChallengee(challengee);
	}

	@Override
	public IDeck getChallengerDeck() {
		return getInnerObject().getChallengerDeck();
	}

	@Override
	public void setChallengerDeck(IDeck challengerDeck) {
		getInnerObject().setChallengerDeck(challengerDeck);
	}

	@Override
	public IDeck getChallengeeDeck() {
		return getInnerObject().getChallengeeDeck();
	}

	@Override
	public void setChallengeeDeck(IDeck challengeeDeck) {
		getInnerObject().setChallengeeDeck(challengeeDeck);
	}

	@Override
	public List<ICardInPlay> getChallengerHand() {
		return getInnerObject().getChallengerHand();
	}

	@Override
	public void setChallengerHand(List<ICardInPlay> challengerHand) {
		getInnerObject().setChallengerHand(challengerHand);
	}

	@Override
	public List<ICardInPlay> getChallengeeHand() {
		return getInnerObject().getChallengeeHand();
	}

	@Override
	public void setChallengeeHand(List<ICardInPlay> challengeeHand) {
		getInnerObject().setChallengeeHand(challengeeHand);
	}

	@Override
	public List<ICardInPlay> getChallengerBench() {
		return getInnerObject().getChallengerBench();
	}

	@Override
	public void setChallengerBench(List<ICardInPlay> challengerBench) {
		getInnerObject().setChallengerBench(challengerBench);
	}

	@Override
	public List<ICardInPlay> getChallengeeBench() {
		return getInnerObject().getChallengeeBench();
	}

	@Override
	public void setChallengeeBench(List<ICardInPlay> challengeeBench) {
		getInnerObject().setChallengeeBench(challengeeBench);
	}

	@Override
	public List<ICardInPlay> getChallengerDiscarded() {
		return getInnerObject().getChallengerDiscarded();
	}

	@Override
	public void setChallengerDiscarded(List<ICardInPlay> challengerDiscarded) {
		getInnerObject().setChallengerDiscarded(challengerDiscarded);
	}

	@Override
	public List<ICardInPlay> getChallengeeDiscarded() {
		return getInnerObject().getChallengeeDiscarded();
	}

	@Override
	public void setChallengeeDiscarded(List<ICardInPlay> challengeeDiscarded) {
		getInnerObject().setChallengeeDiscarded(challengeeDiscarded);
	}

	@Override
	public int getStatus() {
		return getInnerObject().getStatus();
	}

	@Override
	public void setStatus(int status) {
		getInnerObject().setStatus(status);
	}

	@Override
	protected GameBoard getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return GameMapper.buildGameBoard(GameMapper.findById(id));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
