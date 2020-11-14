package games.stendhal.server.entity.item;

import static games.stendhal.common.constants.Actions.BASEITEM;
import static games.stendhal.common.constants.Actions.BASEOBJECT;
import static games.stendhal.common.constants.Actions.BASESLOT;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.actions.UseAction;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.status.StatusType;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.game.RPAction;
import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;

public class PoisonHealerTest {
	
	private static Player player1;
	private static Player player2;

	private static RPAction action1 = new RPAction();
	private static RPAction action2 = new RPAction();

	private static PoisonHealer recover1;
	private static PoisonHealer recover2;

	private static UseAction ua = new UseAction();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		ItemTestHelper.generateRPClasses();
		
		player1 = PlayerTestHelper.createPlayer("bob");
		player2 = PlayerTestHelper.createPlayer("coc");

		
		final StendhalRPZone zone = new StendhalRPZone("zone");
		zone.add(player1);
		zone.add(player2);
		
		recover1 = (PoisonHealer)SingletonRepository.getEntityManager().getItem("poison healer");
		recover2 = (PoisonHealer)SingletonRepository.getEntityManager().getItem("poison healer");

		
		player1.equip("bag", recover1);
		action1.put(BASEITEM, recover1.getID().getObjectID());
		action1.put(BASEOBJECT, player1.getID().getObjectID());
		action1.put(BASESLOT, "bag");
		
		player2.equip("bag", recover2);
		action2.put(BASEITEM, recover2.getID().getObjectID());
		action2.put(BASEOBJECT, player2.getID().getObjectID());
		action2.put(BASESLOT, "bag");
	}
	
	// Test if player have recovering status after using poison healer
	@Test
	public void isRecoveringTest() {
		ua.onAction(player1, action1);
		assertTrue(player1.hasStatus(StatusType.POISONRECOVERING));
	}
	
	// Test player recovering from poison status
	@Test
	public void isRecoveredTest() {
		ConsumableItem poison = (ConsumableItem)SingletonRepository.getEntityManager().getItem("poison");
		player2.equip("bag", poison);
		RPAction action3 = new RPAction();
		action3.put(BASEITEM, poison.getID().getObjectID());
		action3.put(BASEOBJECT, player2.getID().getObjectID());
		action3.put(BASESLOT, "bag");
		ua.onAction(player2, action3);
		assertTrue(player2.hasStatus(StatusType.POISONED));
		
		ua.onAction(player2, action2);
		assertTrue(player2.hasStatus(StatusType.POISONRECOVERING));
		
		assertFalse(player2.hasStatus(StatusType.POISONED));
		
	}
}
