package games.stendhal.server.entity.status;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.rp.StendhalRPAction;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.creature.Sheep;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.CreatureTestHelper;
import utilities.RPClass.ItemTestHelper;

public class HeavyStatusAttackerTest {

	private Creature creature = new Sheep();
	private Player player = PlayerTestHelper.createPlayer("bob");
	private StendhalRPZone arena = new StendhalRPZone("arena");
	private Item item = SingletonRepository.getEntityManager().getItem("wand");
	Player victim = PlayerTestHelper.createPlayer("victim");

	
	/**
	 * initialisation
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		MockStendlRPWorld.get();
		CreatureTestHelper.generateRPClasses();
		ItemTestHelper.generateRPClasses();
		
	}
	
	@AfterClass
	public static void removePlayerAfterClass() throws Exception{
		PlayerTestHelper.removePlayer("bob");
	}
	
	public void createSetup() {
		arena.add(creature);
		arena.add(player);
		creature.setBaseSpeed(5);
		creature.setSpeed(5);
		creature.setBaseHP(10000);
		creature.setHP(10000);
		player.setTarget(creature);
	}
	
	@Test
	public void wandIsInTheBagTest() {
			
		player.equip("bag", item);
		assertTrue(player.isEquipped("wand"));
	}
	
	@Test
	public void wandAttacksTargetTest() {
		
		createSetup();		
		PlayerTestHelper.equipWithItemToSlot(player, "wand", "lhand");
		assertTrue(player.attack());
	
	}
	
	@Test
	public void slowEffectFromWandAttackerTest() {
		
		createSetup();
		player.attack();
		assertTrue(creature.getSpeed() < creature.getBaseSpeed());
		
	}

		
	@Test
	public void wandSetsStatusHeavy(){
		createSetup();
		StendhalRPAction.startAttack(player, victim);
		assertTrue(victim.hasStatus(StatusType.HEAVY));
	}
	
	@Test
	public void testHeavyStatusAttacker() {
		
		final HeavyStatusAttacker attacker = new HeavyStatusAttacker(null, 100);
		attacker.onAttackAttempt(victim, SingletonRepository.getEntityManager().getCreature("snake"));
		assertTrue(victim.hasStatus(StatusType.HEAVY));
	}

	@Test
	public void testHeavyStatusAttackerProbabilityZero() {
		
		final HeavyStatusAttacker attacker = new HeavyStatusAttacker(null, 0); //this one passes
		attacker.onAttackAttempt(victim, SingletonRepository.getEntityManager().getCreature("snake"));
		assertFalse(victim.hasStatus(StatusType.HEAVY));
	}
	
	
}

