package games.stendhal.server.entity.item;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.client.util.UserInterfaceTestHelper;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.status.StatusType;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.events.TurnNotifier;
import games.stendhal.server.core.rp.StendhalRPAction;
import games.stendhal.server.entity.creature.Creature;
import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;



public class WandOfSluggishnessTest {
	private StendhalRPZone zone;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		UserInterfaceTestHelper.initUserInterface();
		MockStendlRPWorld.get();
		ItemTestHelper.generateRPClasses();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		zone = new StendhalRPZone("zone", 20, 20);
		zone.protectionMap.init(1, 1);
		MockStendlRPWorld.get().addRPZone(zone);
	}

	@After
	public void tearDown() throws Exception {
		PlayerTestHelper.removeAllPlayers();
	}
	
	@Test
	public void testWandDescription() {
		final WandOfSluggishness wand = (WandOfSluggishness) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		assertEquals(wand.describe(), "You see a simple metal wand, it feels heavy in your hand and pulses with grey energy.");

	}

	@Test
	public void testWandDoesNoDamage() {
		Player attacker = PlayerTestHelper.createPlayer("attacker");
		attacker.setLevel(100);
		final WandOfSluggishness wand = (WandOfSluggishness) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		attacker.equip("rhand", wand);
		
		Creature target = SingletonRepository.getEntityManager().getCreature("mouse");
		int initialHP = Integer.valueOf(target.getHP());
		
		zone.add(attacker);
		zone.add(target);

		StendhalRPAction.startAttack(attacker, target);
		
		//int currentTurnForDebugging = TurnNotifier.get().getCurrentTurnForDebugging();
		//TurnNotifier.get().logic(currentTurnForDebugging + 1);
		
		int afterHP = Integer.valueOf(target.getHP());
		
		assertEquals(initialHP,afterHP);
		attacker.stopAttack();
	}

	public void testWandAppliesHeavy() {
		Player attacker = PlayerTestHelper.createPlayer("attacker");
		attacker.setLevel(100);
		final WandOfSluggishness wand = (WandOfSluggishness) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		attacker.equip("rhand", wand);
		
		Creature target = SingletonRepository.getEntityManager().getCreature("mouse");
		
		zone.add(attacker);
		zone.add(target);

		StendhalRPAction.startAttack(attacker, target);
		
		
		assertTrue(target.hasStatus(StatusType.HEAVY));
		
		attacker.stopAttack();
	}
	
	public void testWandReducesSpeed() {
		Player attacker = PlayerTestHelper.createPlayer("attacker");
		attacker.setLevel(100);
		final WandOfSluggishness wand = (WandOfSluggishness) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		attacker.equip("rhand", wand);
		
		Creature target = SingletonRepository.getEntityManager().getCreature("mouse");
		double startSpeed = target.getBaseSpeed();
		
		zone.add(attacker);
		zone.add(target);

		StendhalRPAction.startAttack(attacker, target);
		
		double endSpeed = target.getBaseSpeed();
		
		assertNotEquals(startSpeed, endSpeed);
		
		attacker.stopAttack();
	}
}

