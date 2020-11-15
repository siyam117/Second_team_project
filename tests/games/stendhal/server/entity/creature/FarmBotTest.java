package games.stendhal.server.entity.creature;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.mapstuff.spawner.GrainField;
import games.stendhal.server.entity.mapstuff.spawner.PassiveEntityRespawnPoint;
import games.stendhal.server.entity.mapstuff.spawner.PassiveEntityRespawnPointFactory;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.CreatureTestHelper;

public class FarmBotTest {
	private StendhalRPZone zone;
	private FarmBot farmBot;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		PlayerTestHelper.generatePlayerRPClasses();
		CreatureTestHelper.generateRPClasses();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		MockStendlRPWorld.reset();
	}
	
	@Before
	public void initializeZone() {
		MockStendlRPWorld.reset();
		final StendhalRPZone zone = new StendhalRPZone("testzone", 10, 10);
		MockStendlRPWorld.get().addRPZone(zone);
		
		final FarmBot farmBot = new FarmBot();
		farmBot.setPosition(0, 0);
		assertNotNull(farmBot);
		zone.add(farmBot);
	}
	
	@Test
	public void testGetNearestGrainFieldCrop() {
		final StendhalRPZone zone = new StendhalRPZone("testzone", 10, 10);
		MockStendlRPWorld.get().addRPZone(zone);
		
		final GrainField cropClose = new GrainField(null, null);
		cropClose.setPosition(0, 0);
		assertNotNull(cropClose);
		zone.add(cropClose);
		
		final GrainField cropFar = new GrainField(null, null);
		cropFar.setPosition(10, 10);
		assertNotNull(cropFar);
		zone.add(cropFar);
		
		assertSame(cropClose, farmBot.getNearestHarvestableCrop());
	}
	
	@Test
	public void testCanHarvestTypeGrainField() {
		assertFalse(farmBot.canHarvest(null));
		
		final PassiveEntityRespawnPoint crop = PassiveEntityRespawnPointFactory.create("corn", 0, null, 0, 0);
		zone.add(crop);
		
		assertFalse(farmBot.canHarvest(crop));
		
		farmBot.equip("lhand", SingletonRepository.getEntityManager().getItem("scythe"));
		assertTrue(farmBot.canHarvest(crop));
		
		crop.setPosition(10, 10);
		assertFalse(farmBot.canHarvest(crop));
	}
	
	@Test
	public void testCanHarvestTypeVegetableGrower() {
		assertFalse(farmBot.canHarvest(null));
		
		final PassiveEntityRespawnPoint crop = PassiveEntityRespawnPointFactory.create("vegetable", 0, null, 0, 0);
		zone.add(crop);
		
		assertTrue(farmBot.canHarvest(crop));
		
		crop.setPosition(10, 10);
		assertFalse(farmBot.canHarvest(crop));
	}
}
