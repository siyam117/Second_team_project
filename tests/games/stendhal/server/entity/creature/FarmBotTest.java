package games.stendhal.server.entity.creature;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.mapstuff.spawner.GrainField;
import games.stendhal.server.entity.mapstuff.spawner.PassiveEntityRespawnPoint;
import games.stendhal.server.entity.mapstuff.spawner.PassiveEntityRespawnPointFactory;
import games.stendhal.server.entity.mapstuff.spawner.VegetableGrower;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.RPClass.CreatureTestHelper;
import utilities.RPClass.GrowingPassiveEntityRespawnPointTestHelper;

public class FarmBotTest {
	private StendhalRPZone zone;
	private FarmBot farmBot;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		CreatureTestHelper.generateRPClasses();
		GrowingPassiveEntityRespawnPointTestHelper.generateRPClasses();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		MockStendlRPWorld.reset();
	}
	
	@Before
	public void initialize() {
		MockStendlRPWorld.reset();
		this.zone = new StendhalRPZone("testzone", 10, 10);
		MockStendlRPWorld.get().addRPZone(zone);
		
		this.farmBot = new FarmBot();
		farmBot.setPosition(0, 0);
		assertNotNull(farmBot);
		zone.add(farmBot);
	}
	
	@Test
	public void testCorrectEquippedTools() {
		assertTrue(farmBot.isEquipped("scythe"));
		assertTrue(farmBot.isEquipped("sickle"));
	}
	
	@Test
	public void testGetNearestHarvestableCrop() {
		assertNull(farmBot.getNearestHarvestableCrop());
		
		final PassiveEntityRespawnPoint cropClose = PassiveEntityRespawnPointFactory.create("vegetable", 1, null, 0, 0);
		assertNotNull(cropClose);
		cropClose.onFruitPicked(null);
		zone.add(cropClose);
		
		final PassiveEntityRespawnPoint cropFar = PassiveEntityRespawnPointFactory.create("vegetable", 1, null, 10, 10);
		cropFar.setPosition(10, 10);
		assertNotNull(cropFar);
		cropFar.onFruitPicked(null);
		zone.add(cropFar);
		
		assertNull(farmBot.getNearestHarvestableCrop());
		
		cropFar.setToFullGrowth();
		assertSame(cropFar, farmBot.getNearestHarvestableCrop());
		
		cropClose.setToFullGrowth();
		assertSame(cropClose, farmBot.getNearestHarvestableCrop());
	}
	
	@Test
	public void testCanHarvestTypeGrainField() {
		assertFalse(farmBot.canHarvest(null));
		
		final GrainField crop = (GrainField) PassiveEntityRespawnPointFactory.create("corn", 0, null, 0, 0);
		zone.add(crop);
		crop.onFruitPicked(null);
		
		assertFalse(farmBot.canHarvest(crop));

		crop.setToFullGrowth();
		assertTrue(farmBot.canHarvest(crop));
		assertTrue(crop.onUsed(farmBot));
		
		crop.setPosition(10, 10);
		crop.setToFullGrowth();
		assertFalse(farmBot.canHarvest(crop));
		assertFalse(crop.onUsed(farmBot));
	}
	
	@Test
	public void testCanHarvestTypeVegetableGrower() {
		assertFalse(farmBot.canHarvest(null));
		
		final VegetableGrower crop = (VegetableGrower) PassiveEntityRespawnPointFactory.create("vegetable", 2, null, 0, 0);
		zone.add(crop);
		crop.onFruitPicked(null);
		
		assertFalse(farmBot.canHarvest(crop));

		crop.setToFullGrowth();
		assertTrue(farmBot.canHarvest(crop));
		assertTrue(crop.onUsed(farmBot));
		
		crop.setPosition(10, 10);
		crop.setToFullGrowth();
		assertFalse(farmBot.canHarvest(crop));
		assertFalse(crop.onUsed(farmBot));
	}
	
	@Test
	public void testHarvestsCrops() {
		PassiveEntityRespawnPoint crop = PassiveEntityRespawnPointFactory.create("vegetable", 2, null, 0, 0);
		zone.add(crop);
		
		crop.setToFullGrowth();
		farmBot.logic();
		
		assertThat(crop.getInt("ripeness"),is(0));
	}
}
