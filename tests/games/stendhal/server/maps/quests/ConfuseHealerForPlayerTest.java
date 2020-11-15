package games.stendhal.server.maps.quests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendhalRPRuleProcessor;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.deniran.cityinterior.hospital.ConfuseDoctorNPC;
import games.stendhal.server.maps.deniran.cityinterior.hospital.ConfuseNurseNPC;
import games.stendhal.server.maps.quests.deniran.hospital.ConfuseHealerForPlayer;
import marauroa.common.Log4J;
import marauroa.common.game.RPObject.ID;
import utilities.PlayerTestHelper;

public class ConfuseHealerForPlayerTest {

	private SpeakerNPC doctor;
	private SpeakerNPC nurse;
	private SpeakerNPC nurse2;


	private ConfuseHealerForPlayer phfp;
	
	private Player player = null;
	private Engine en = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Log4J.init();

		MockStendhalRPRuleProcessor.get();

		MockStendlRPWorld.reset();
		MockStendlRPWorld.get();
	}

	@Before
	public void setup() {
		PlayerTestHelper.removeAllPlayers();
		StendhalRPZone zone = new StendhalRPZone("admin_test");
		new ConfuseDoctorNPC().configureZone(zone, null);
		new ConfuseNurseNPC().configureZone(zone, null);
		doctor = SingletonRepository.getNPCList().get("Confuse Doctor") ;
		nurse = SingletonRepository.getNPCList().get("Confuse Nurse") ;
		nurse2 = SingletonRepository.getNPCList().get("Confuse Nurse") ;

		phfp = new ConfuseHealerForPlayer();

		phfp.addToWorld();
	}

	@Test
	public void testQuestWithEnoughMoney() {

		player = PlayerTestHelper.createPlayer("player");

		en = doctor.getEngine();
		
		en.step(player, "hi");
		assertEquals("Hi, can I #help you?", getReply(doctor));
		
		en.step(player, "help");
		assertEquals("You can go talk to the nurse to get some medicine", getReply(doctor));
		// accept
		en.step(player, "yes");
		assertTrue(player.hasQuest("healer_player"));
		en.step(player, "bye");
		assertFalse(doctor.isTalking());
		assertEquals("start", player.getQuest("healer_player"));
		en = nurse.getEngine();
		final StackableItem money = (StackableItem)SingletonRepository.getEntityManager().getItem("money");
		money.setQuantity(500);
		money.setID(new ID(2, "testzone"));
		player.getSlot("bag").add(money);
		assertEquals(500, player.getNumberOfEquipped("money"));
		en.step(player, "hi");
		assertEquals(
				"Here, take this medicine",
				getReply(nurse));
		en.step(player, "yes");
		assertEquals(
				"Take this medicine and have a rest for a while",
				getReply(nurse));
		assertEquals("done", player.getQuest("healer_player"));
		en.step(player, "bye");
		
		// reject
		final Player player2 = PlayerTestHelper.createPlayer("player");
		
		en = doctor.getEngine();
		
		en.step(player, "hi");
		assertEquals("Hi, can I #help you?", getReply(doctor));
		
		en.step(player, "help");
		assertEquals(
				"You can go talk to the nurse to get some medicine",
				getReply(doctor));

		//en.step(player2, "help");
		//assertEquals(
		//		"I see you are poisoned, go down to get treatment from nurse",
		//		getReply(doctor));
		en.step(player2, "no");
		assertTrue(player2.hasQuest("healer_player"));
		assertEquals("rejected", player2.getQuest("healer_player"));
		en.step(player2, "bye");
	}
	
	@Test
	public void testQuestWithNotEnoughMoney() {

		final Player player3 = PlayerTestHelper.createPlayer("player");

		en = doctor.getEngine();
		
		en.step(player, "hi");
		assertEquals("Hi, can I #help you?", getReply(doctor));
		
		en.step(player, "help");
		assertEquals(
				"You can go talk to the nurse to get some medicine",
				getReply(doctor));
		
		//en.step(player3, "help");
		//assertEquals(
				//"I see you are poisoned, go down to get treatment from nurse",
				//getReply(doctor));
		// accept
		en.step(player3, "yes");
		assertTrue(player3.hasQuest("healer_player"));
		en.step(player3, "bye");
		assertFalse(doctor.isTalking());
		assertEquals("start", player3.getQuest("healer_player"));
		en = nurse2.getEngine();
		assertNotEquals(500, player3.getNumberOfEquipped("money"));
		en.step(player3, "hi");
		assertEquals(
				"You need to have 500 money, otherwise we are not able to help you",
				getReply(nurse2));
		en.step(player3, "bye");
	}
	


	/**
	 * Tests for getHistory.
	 */
	@Test
	public void testgetHistory() {
		final Player player = PlayerTestHelper.createPlayer("bob");
		assertTrue(phfp.getHistory(player).isEmpty());
		player.setQuest("healer_player", "");
		final List<String> history = new LinkedList<String>();
		history.add("I have talked to Confuse Doctor.");
		assertEquals(history, phfp.getHistory(player));

		player.setQuest("healer_player", "rejected");
		history.add("I do not want to waste my money.");
		assertEquals(history, phfp.getHistory(player));

		player.setQuest("healer_player", "start");
		history.remove("I do not want to waste my money.");
		history.add("I go to find Nurse.");
		assertEquals(history, phfp.getHistory(player));

		player.equipToInventoryOnly(SingletonRepository.getEntityManager().getItem("money"));
		history.add("I have enough money.");
		assertEquals(history, phfp.getHistory(player));
		player.setQuest("healer_player", "done");
		history.add("I gave the money to Nurse. She gave me medicine.");
		assertEquals(history, phfp.getHistory(player));

	}



}
