package games.stendhal.server.entity.item;

import static games.stendhal.common.constants.Actions.BASEITEM;
import static games.stendhal.common.constants.Actions.BASEOBJECT;
import static games.stendhal.common.constants.Actions.BASESLOT;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import games.stendhal.common.constants.Actions;
import games.stendhal.server.actions.UseAction;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.status.ConfuseRecoveringStatus;
import games.stendhal.server.entity.status.ConfuseStatus;
import games.stendhal.server.entity.status.StatusType;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.game.RPAction;
import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;

public class ConfuseHealerTest {
	
	private static Player player1;
	private static Player player2;
	private static Player player3;
	private static Player player4;
	private static Player player5;
	
	private static RPAction action1 = new RPAction();
	private static RPAction action2 = new RPAction();
	private static RPAction action3 = new RPAction();
	private static RPAction action4 = new RPAction();
	private static RPAction action5 = new RPAction();
	
	private static ConfuseHealer recover1;
	private static ConfuseHealer recover2;
	private static ConfuseHealer recover3;
	private static ConfuseHealer recover4;
	private static ConfuseHealer recover5;

	
	private static UseAction ua = new UseAction();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		ItemTestHelper.generateRPClasses();
		
		player1 = PlayerTestHelper.createPlayer("bob");
		player2 = PlayerTestHelper.createPlayer("coc");
		player3 = PlayerTestHelper.createPlayer("dod");
		player4 = PlayerTestHelper.createPlayer("eoe");
		player5 = PlayerTestHelper.createPlayer("fof");
		
		final StendhalRPZone zone = new StendhalRPZone("zone");
		zone.add(player1);
		zone.add(player2);
		zone.add(player3);
		zone.add(player4);
		zone.add(player5);
		
		recover1 = (ConfuseHealer)SingletonRepository.getEntityManager().getItem("confuse healer");
		recover2 = (ConfuseHealer)SingletonRepository.getEntityManager().getItem("confuse healer");
		recover3 = (ConfuseHealer)SingletonRepository.getEntityManager().getItem("confuse healer");
		recover4 = (ConfuseHealer)SingletonRepository.getEntityManager().getItem("confuse healer");
		recover5 = (ConfuseHealer)SingletonRepository.getEntityManager().getItem("confuse healer");
		
		player1.equip("bag", recover1);
		action1.put(BASEITEM, recover1.getID().getObjectID());
		action1.put(BASEOBJECT, player1.getID().getObjectID());
		action1.put(BASESLOT, "bag");
		
		player2.equip("bag", recover2);
		action2.put(BASEITEM, recover2.getID().getObjectID());
		action2.put(BASEOBJECT, player2.getID().getObjectID());
		action2.put(BASESLOT, "bag");
		
		player3.equip("bag", recover3);
		action3.put(BASEITEM, recover3.getID().getObjectID());
		action3.put(BASEOBJECT, player3.getID().getObjectID());
		action3.put(BASESLOT, "bag");
		
		player4.equip("bag", recover4);
		action4.put(BASEITEM, recover4.getID().getObjectID());
		action4.put(BASEOBJECT, player4.getID().getObjectID());
		action4.put(BASESLOT, "bag");
		
		player5.equip("bag", recover5);
		action5.put(BASEITEM, recover5.getID().getObjectID());
		action5.put(BASEOBJECT, player5.getID().getObjectID());
		action5.put(BASESLOT, "bag");
	}
	
	// Test if confuse healer is still there after been used
	@Test
	public void isConsumableTest() {
		assertTrue(player1.isEquipped("confuse healer"));
		ua.onAction(player1, action1);
		assertFalse(player1.isEquipped("confuse healer"));

	}
	
	// Test if player are recovering after using confuse healer
	@Test
	public void isRecoveringTest() {
		ua.onAction(player2, action2);
		assertTrue(player2.hasStatus(StatusType.CONFUSERECOVERING));
	}
	
	// Test if player are healing after using confuse recovering
	@Test
	public void isHealingTest() {
		ua.onAction(player3, action3);
		assertTrue(player3.hasStatus(StatusType.EATING));
	}
	
	// Test if player are unable to move when they are healing
	@Test
	public void isMoveableTest() {
		ua.onAction(player4, action4);
		assertFalse(player4.has(Actions.AUTOWALK));
	}
	
	// Test player recovering from confuse status
	@Test
	public void isRecoveredTest() {
		Creature attacker = SingletonRepository.getEntityManager().getCreature("chaos sorcerer");
		ConfuseStatus confuse = new ConfuseStatus();
		player5.getStatusList().inflictStatus(confuse, attacker);
		assertTrue(player5.hasStatus(StatusType.CONFUSED));
		
		ConfuseRecoveringStatus confuse_recoveringstatus = new ConfuseRecoveringStatus();
		confuse_recoveringstatus.setStepsDelay(0);
		player5.getStatusList().inflictStatus(confuse_recoveringstatus, recover5);
		assertTrue(player5.hasStatus(StatusType.CONFUSERECOVERING));
		player5.getStatusList().remove(confuse_recoveringstatus);
		assertFalse(player5.hasStatus(StatusType.CONFUSED));	
	}
}