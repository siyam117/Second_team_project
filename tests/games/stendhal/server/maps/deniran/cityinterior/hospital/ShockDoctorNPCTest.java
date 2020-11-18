package games.stendhal.server.maps.deniran.cityinterior.hospital;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import static utilities.SpeakerNPCTestHelper.getReply;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;



public class ShockDoctorNPCTest extends ZonePlayerAndNPCTestImpl{
	
	private static final String ZONE_NAME = "int_deniran_hospital";
	//private static SpeakerNPC speakerTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		setupZone(ZONE_NAME);
	}
	
	public ShockDoctorNPCTest() {
		setNpcNames("Shock Doctor");
		setZoneForPlayer(ZONE_NAME);
		addZoneConfigurator(new ShockDoctorNPC(), ZONE_NAME);
	}	
	@Test
	public void createDialogTest() {
		
		final SpeakerNPC speakerNPCTest = getNPC("Shock Doctor");
		assertNotNull(speakerNPCTest);
		
		final Engine engineTest = speakerNPCTest.getEngine();
		engineTest.step(player, "hi");
		assertTrue(speakerNPCTest.isTalking());
		String replay = getReply(speakerNPCTest);
		assertNotNull(replay);
		assertEquals("Hi, can I #help you?", replay);
		
		engineTest.step(player, "bye");
		assertFalse(speakerNPCTest.isTalking());
		assertEquals("Bye.", getReply(speakerNPCTest));
	}

}
