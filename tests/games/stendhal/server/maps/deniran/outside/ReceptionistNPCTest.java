package games.stendhal.server.maps.deniran.outside;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import static utilities.SpeakerNPCTestHelper.getReply;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.maps.deniran.ReceptionistNPC;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;



public class ReceptionistNPCTest extends ZonePlayerAndNPCTestImpl{
	
	private static final String ZONE_NAME = "0_deniran_city_sw";
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		setupZone(ZONE_NAME);
	}
	
	public ReceptionistNPCTest() {
		setNpcNames("Receptionist");
		setZoneForPlayer(ZONE_NAME);
		addZoneConfigurator(new ReceptionistNPC(), ZONE_NAME);
	}	
	@Test
	public void createDialogTest() {
		
		final SpeakerNPC speakerNPCTest = getNPC("Receptionist");
		assertNotNull(speakerNPCTest);
		
		final Engine engineTest = speakerNPCTest.getEngine();
		engineTest.step(player, "hi");
		assertTrue(speakerNPCTest.isTalking());
		String replay = getReply(speakerNPCTest);
		assertNotNull(replay);
		assertEquals("Hello and welcome to Deniran Hospital. I will sell #fruits. #help for more", replay);
		
		engineTest.step(player, "help");
		assertTrue(speakerNPCTest.isTalking());
		replay = getReply(speakerNPCTest);
		assertNotNull(replay);
		assertEquals("We will open soon, if you interesting in this, contact us to be a admin to test", replay);
		
		engineTest.step(player, "fruit");
		assertTrue(speakerNPCTest.isTalking());
		replay = getReply(speakerNPCTest);
		assertNotNull(replay);
		assertEquals("We only offer #apple, #carrot now, future will have more fruits and flowers and get well soon card", replay);
		
		engineTest.step(player, "bye");
		assertFalse(speakerNPCTest.isTalking());
		assertEquals("Bye.", getReply(speakerNPCTest));
	}

}

