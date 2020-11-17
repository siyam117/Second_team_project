package games.stendhal.server.entity.creature;

import java.util.List;

import org.apache.log4j.Logger;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.mapstuff.spawner.PassiveEntityRespawnPoint;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.Definition.Type;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;
import marauroa.common.game.SyntaxException;

public class FarmBot extends DomesticAnimal {
	
	private static final Logger logger = Logger.getLogger(FarmBot.class);
	private List<String> tools;
	
	public static void generateRPClass() {
		try {
			final RPClass farmBot = new RPClass("farmbot");
			farmBot.isA("creature");
			farmBot.addAttribute("weight", Type.BYTE);
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
			//owner.setFarmBot(this); //TODO
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
		return null;
		// TODO Auto-generated method stub
		
	}
	
	protected boolean canHarvest(PassiveEntityRespawnPoint crop) {
		if (crop == null) {
			return false;
		}
		return this.nextTo(crop) && capableOfHarvesting(crop);
	}

	protected boolean capableOfHarvesting(PassiveEntityRespawnPoint crop) {
		if(!crop.has("ripeness")) {
			return false;
		}
		if (crop.getInt("ripeness") != crop.getInt("max_ripeness")) {
			return false;
		}
		
		return true;
	}
}
