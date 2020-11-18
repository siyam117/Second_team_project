package games.stendhal.server.entity.item;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.client.util.UserInterfaceTestHelper;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.status.StatusType;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
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
		final StackableItem wand = (StackableItem) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		assertEquals(wand.describe(), "You see a simple metal wand, it feels heavy in your hand and pulses with grey energy. Stats are (ATK: -1 RANGE: 5).");
	}
	
	@Test
	public void wandIsInTheBagTest() {
		Player holder = PlayerTestHelper.createPlayer("holder");
		final StackableItem wand = (StackableItem) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		holder.equip("bag", wand);
		assertTrue(holder.isEquipped("wand of sluggishness"));
	}


	@Test
	public void testWandDoesNoDamage() {
		Player attacker = PlayerTestHelper.createPlayer("attacker");
		attacker.setLevel(100);
		final StackableItem wand = (StackableItem) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		attacker.equip("rhand", wand);
		
		Creature target = SingletonRepository.getEntityManager().getCreature("mouse");
		int initialHP = Integer.valueOf(target.getHP());
		
		zone.add(attacker);
		zone.add(target);

		StendhalRPAction.playerAttack(attacker, target);
		
		
		int afterHP = Integer.valueOf(target.getHP());
		
		assertEquals(initialHP,afterHP);
		attacker.stopAttack();
	}

	@Test
	public void testWandAppliesHeavy() {
		Player attacker = PlayerTestHelper.createPlayer("attacker");
		attacker.setLevel(100);
		final StackableItem wand = (StackableItem) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		attacker.equip("rhand", wand);
		
		Creature target = SingletonRepository.getEntityManager().getCreature("mouse");
		
		zone.add(attacker);
		zone.add(target);

		StendhalRPAction.playerAttack(attacker, target);
		
		
		assertTrue(target.hasStatus(StatusType.HEAVY));
		
		attacker.stopAttack();
	}
	
	@Test
	public void testWandReducesSpeed() {
		Player attacker = PlayerTestHelper.createPlayer("attacker");
		attacker.setLevel(100);
		final StackableItem wand = (StackableItem) SingletonRepository.getEntityManager().getItem("wand of sluggishness");
		attacker.equip("rhand", wand);
		
		Creature target = SingletonRepository.getEntityManager().getCreature("mouse");
		double startSpeed = target.getBaseSpeed();
		
		zone.add(attacker);
		zone.add(target);

		StendhalRPAction.playerAttack(attacker, target);
		
		double endSpeed = target.getBaseSpeed();
		
		assertNotEquals(startSpeed, endSpeed);
		
		attacker.stopAttack();
	}
}

