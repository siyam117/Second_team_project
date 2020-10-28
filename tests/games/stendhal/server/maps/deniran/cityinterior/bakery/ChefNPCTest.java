package games.stendhal.server.maps.deniran.cityinterior.bakery;

/*
Added by Angela Popovska 26/10/2020
*/
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



public class ChefNPCTest extends ZonePlayerAndNPCTestImpl{
	
	private static final String ZONE_NAME = "Deniran";
	private static final String nameTest = "Patrick";
	//private static SpeakerNPC speakerTest;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		setupZone(ZONE_NAME);
	}
	
	public ChefNPCTest() {
		setNpcNames("Patrick");
		setZoneForPlayer(ZONE_NAME);
		addZoneConfigurator(new ChefNPC(), ZONE_NAME);
	}	
	@Test
	public void createDialogTest() {
		
		final SpeakerNPC speakerNPCTest = getNPC("Patrick");
		assertNotNull(speakerNPCTest);
		
		final Engine engineTest = speakerNPCTest.getEngine();
		engineTest.step(player, "hi");
		assertTrue(speakerNPCTest.isTalking());
		String replay = getReply(speakerNPCTest);
		assertNotNull(replay);
		assertEquals("Hello and welcome to Deniran Bakery.", replay);
				
		engineTest.step(player, "job");
		assertTrue(speakerNPCTest.isTalking());
		replay = getReply(speakerNPCTest);
		assertNotNull(replay);
		assertEquals("I run Deniran Bakery. ", replay);
		
		engineTest.step(player, "bye");
		assertFalse(speakerNPCTest.isTalking());
		assertEquals("Bye.", getReply(speakerNPCTest));
	}

}