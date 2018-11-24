package dom.model.attachedenergy;

import org.dsrg.soenea.domain.DomainObjectCreationException;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

import dom.model.attachedenergy.mapper.AttachedEnergyInputMapper;
import dom.model.bench.IBench;
import dom.model.game.IGame;
import dom.model.user.IUser;

public class AttachedEnergyProxy extends DomainObjectProxy<Long, AttachedEnergy> implements IAttachedEnergy {

	protected AttachedEnergyProxy(Long id) {
		super(id);
	}
	
	@Override
	public IGame getGame() {
		return getInnerObject().getGame();
	}

	@Override
	public void setGame(IGame game) {
		getInnerObject().setGame(game);
	}

	@Override
	public long getGameVersion() {
		return getInnerObject().getGameVersion();
	}

	@Override
	public void setGameVersion(long gameVersion) {
		getInnerObject().setGameVersion(gameVersion);
	}

	@Override
	public IUser getPlayer() {
		return getInnerObject().getPlayer();
	}

	@Override
	public void setPlayer(IUser player) {
		getInnerObject().setPlayer(player);
	}

	@Override
	public IBench getEnergyCard() {
		return getInnerObject().getEnergyCard();
	}

	@Override
	public void setEnergyCard(IBench energyCard) {
		getInnerObject().setEnergyCard(energyCard);
	}

	@Override
	public IBench getPokemonCard() {
		return getInnerObject().getPokemonCard();
	}

	@Override
	public void setPokemonCard(IBench pokemonCard) {
		getInnerObject().setPokemonCard(pokemonCard);
	}

	@Override
	protected AttachedEnergy getFromMapper(Long id) throws MapperException, DomainObjectCreationException {
		try {
			return AttachedEnergyInputMapper.findById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
