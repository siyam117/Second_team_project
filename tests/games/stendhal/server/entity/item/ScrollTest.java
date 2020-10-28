package games.stendhal.server.entity.item;

import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.actions.UseAction;
import utilities.RPClass.ItemTestHelper;
import games.stendhal.server.entity.item.scroll.BlankPetScroll;
import games.stendhal.server.core.engine.SingletonRepository;
import marauroa.common.game.RPAction;
import static games.stendhal.common.constants.Actions.BASEITEM;
import static games.stendhal.common.constants.Actions.BASEOBJECT;
import static games.stendhal.common.constants.Actions.BASESLOT;


public class ScrollTest{
	//private static final String BASEITEM = null;
	//private static final String BASEOBJECT = null;
	//private static final String BASESLOT = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		MockStendlRPWorld.get();
		ItemTestHelper.generateRPClasses();
	}

	@Test
	public void testScrollStillInBag(){
		final Player player = PlayerTestHelper.createPlayer("testPlayer");

		final StendhalRPZone zone = new StendhalRPZone("test zone");
		final BlankPetScroll scroll = (BlankPetScroll) SingletonRepository.getEntityManager().getItem("blank pet scroll");
		
		zone.add(player);
		player.equip("bag", scroll);

		final UseAction use = new UseAction();
		RPAction action = new RPAction();

		action.put(BASEITEM, scroll.getID().getObjectID());
		action.put(BASEOBJECT, player.getID().getObjectID());
		action.put(BASESLOT, "bag");
		use.onAction(player, action);

		assertTrue(player.isEquipped("blank pet scroll"));

	}
}