package games.stendhal.server.entity.creature;



import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.ConsumableItem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.ConsumableTestHelper;
import utilities.RPClass.PetTestHelper;

public class PetTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		PlayerTestHelper.generatePlayerRPClasses();
		PetTestHelper.generateRPClasses();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		MockStendlRPWorld.reset();
	}
	
	private class MockPet extends Pet {
		@Override
		protected List<String> getFoodNames() {
			return null;
		}

		@Override
		void setUp() {
		}
		
		MockPet(int hp, int baseHp, Player pl) {
			this.setOwner(pl);
			this.initHP(baseHp);
			this.setHP(hp);
		}
	}
	
	@Test
	public void testPotionNotConsumedWithHighHPPet() {
		final StendhalRPZone zone = new StendhalRPZone("testzone", 10, 10);
		MockStendlRPWorld.get().addRPZone(zone);

		final Player player = PlayerTestHelper.createPlayer("player");
		zone.add(player);
		
		final Pet pet = new MockPet(100, 101, player);
		zone.add(pet);
		
		final ConsumableItem potion = ConsumableTestHelper.createEater("potion");
		zone.add(potion);
		
		assertThat(zone.getItemsOnGround().size(), is(1));
		pet.logic();
		assertThat(pet.getHP(), is(100));
		assertThat(zone.getItemsOnGround().size(), is(1));
	}
}
