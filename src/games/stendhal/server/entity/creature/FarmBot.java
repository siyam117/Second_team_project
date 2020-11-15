package games.stendhal.server.entity.creature;

import games.stendhal.server.entity.mapstuff.spawner.PassiveEntityRespawnPoint;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPObject;

public class FarmBot extends DomesticAnimal {

	public FarmBot() {
		// TODO Auto-generated constructor stub
	}

	public FarmBot(RPObject object) {
		super(object);
		// TODO Auto-generated constructor stub
	}

	public FarmBot(RPObject object, Player owner) {
		super(object, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	void setUp() {
		// TODO Auto-generated method stub
	}

	protected PassiveEntityRespawnPoint getNearestHarvestableCrop() {
		return null;
		// TODO Auto-generated method stub
		
	}

	protected boolean canHarvest(PassiveEntityRespawnPoint crop) {
		// TODO Auto-generated method stub
		return false;
	}

	protected void equipCorrectTool(PassiveEntityRespawnPoint crop) {
		// TODO Auto-generated method stub
		
	}
}
