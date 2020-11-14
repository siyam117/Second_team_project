package games.stendhal.server.maps.quests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

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
import games.stendhal.server.maps.deniran.cityinterior.hospital.PoisonDoctorNPC;
import games.stendhal.server.maps.deniran.cityinterior.hospital.PoisonNurseNPC;
import games.stendhal.server.maps.quests.deniran.hospital.PoisonHealerForPlayer;
import marauroa.common.Log4J;
import marauroa.common.game.RPObject.ID;
import utilities.PlayerTestHelper;

public class PoisonHealerForPlayerTest {

	private SpeakerNPC doctor;
	private SpeakerNPC nurse;

	private PoisonHealerForPlayer phfp;
	
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
		new PoisonDoctorNPC().configureZone(zone, null);
		new PoisonNurseNPC().configureZone(zone, null);
		doctor = SingletonRepository.getNPCList().get("Poison Doctor") ;
		nurse = SingletonRepository.getNPCList().get("Poison Nurse") ;

		phfp = new PoisonHealerForPlayer();

		phfp.addToWorld();
	}

	@Test
	public void testQuestWithEnoughMoney() {

		player = PlayerTestHelper.createPlayer("player");

		en = doctor.getEngine();
		
		en.step(player, "hi");
		assertEquals("Hi, do you need any #help", getReply(doctor));
		
		en.step(player, "help");
		assertEquals(
				"I see you are poisoned, go down to get treatment from nurse",
				getReply(doctor));
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
				"Come here, I will give you medicine",
				getReply(nurse));
		en.step(player, "yes");
		assertEquals(
				"Take this medicine and have a rest for a while",
				getReply(nurse));
		assertEquals("done", player.getQuest("healer_player"));
		en.step(player, "bye");
	}
}
