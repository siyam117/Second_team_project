package games.stendhal.server.entity.creature;

import org.apache.log4j.Logger;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.mapstuff.spawner.GrainField;
import games.stendhal.server.entity.mapstuff.spawner.PassiveEntityRespawnPoint;
import games.stendhal.server.entity.mapstuff.spawner.VegetableGrower;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;
import marauroa.common.game.SyntaxException;

public class FarmBot extends DomesticAnimal {
	
	private static final Logger logger = Logger.getLogger(FarmBot.class);
	
	public static void generateRPClass() {
		try {
			final RPClass farmBot = new RPClass("farmbot");
			farmBot.isA("creature");
		} catch (final SyntaxException e) {
			logger.error("cannot generate RPClass", e);
		}
	}

	public FarmBot() {
		this(null);
	}

	public FarmBot(RPObject object) {
		this(object, null);
	}
	
	public FarmBot(Player owner) {
		super();
		super.setOwner(owner);
		
		setRPClass("farmbot");
		put("type", "farmbot");
		setUp();
		
		if (owner != null) {
			owner.getZone().add(this);
		}
		
		update();
		logger.debug("Created FarmBot: " + this);
	}

	public FarmBot(RPObject object, Player owner) {
		super(object, owner);
		super.setOwner(owner);

		setRPClass("farmbot");
		put("type", "farmbot");
		setUp();

		if (owner != null) {
			owner.getZone().add(this);
			//owner.setFarmBot(this);
		}

		update();
		logger.debug("Created FarmBot: " + this);
	}

	@Override
	void setUp() {
		baseSpeed = 1;
		// set the default perception range
		setPerceptionRange(20);

		// set the default movement range
		setMovementRange(20);
		addSlot("lhand");
		addSlot("rhand");
		equipOrPutOnGround(SingletonRepository.getEntityManager().getItem("scythe"));
		equipOrPutOnGround(SingletonRepository.getEntityManager().getItem("sickle"));
	}

	protected PassiveEntityRespawnPoint getNearestHarvestableCrop() {
		int range = getMovementRange();
		double squaredDistance = range * range;

		PassiveEntityRespawnPoint chosen = null;

		for (final PassiveEntityRespawnPoint crop : getZone().getPlantGrowers()) {
			if (capableOfHarvesting(crop) && (squaredDistance(crop) < squaredDistance)) {
				chosen = crop;
				squaredDistance = squaredDistance(crop);
			}
		}
		return chosen;
	}
	
	protected boolean canHarvest(PassiveEntityRespawnPoint crop) {
		if (crop == null) {
			return false;
		}
		return nextTo(crop) && capableOfHarvesting(crop);
	}

	protected boolean capableOfHarvesting(PassiveEntityRespawnPoint crop) {
		if(!crop.has("ripeness")) {
			return false;
		}
		if (!(crop instanceof GrainField || crop instanceof VegetableGrower)) {
			return false;
		}
		if (crop.getInt("ripeness") != crop.getInt("max_ripeness")) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void logic() {
		super.logic();
		PassiveEntityRespawnPoint crop = getNearestHarvestableCrop();
		if(canHarvest(crop)) {
			if (crop instanceof VegetableGrower) {
				((VegetableGrower) crop).onUsed(this);
			}
			if (crop instanceof GrainField) {
				((GrainField) crop).onUsed(this);
			}
			clearPath();
		} else {
			if(crop != null) {
				setMovement(crop, 0, 0, getMovementRange());
			}
		}
		applyMovement();
		notifyWorldAboutChanges();
	}

	/**
	 * Does this domestic animal take part in combat?
	 *
	 * @return true, if it can be attacked by creatures, false otherwise
	 */
	@Override
	protected boolean takesPartInCombat() {
		return false;
	}
}
