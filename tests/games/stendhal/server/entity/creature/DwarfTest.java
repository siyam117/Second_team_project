package games.stendhal.server.entity.creature;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.RPClass.CreatureTestHelper;

/**
 * Tests for the speed of all similar classes of dwarves (all under level 30),
 * The speed of them should be set to 0.8.
 */
public class DwarfTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		CreatureTestHelper.generateRPClasses();
	}
	
	/**
	 * Tests for dwarf(level 15).
	 */
	@Test
	public void testDwarfSpeed() {
		Creature creature = new Creature();
		creature = SingletonRepository.getEntityManager().getCreature("dwarf");
		assertThat(creature.getBaseSpeed(), is(0.8));
	}
	
	/**
	 * Tests for dwarf guardian(level 18).
	 */
	@Test
	public void testDwarfGuardianSpeed() {
		Creature creature = new Creature();
		creature = SingletonRepository.getEntityManager().getCreature("dwarf guardian");
		assertThat(creature.getBaseSpeed(), is(0.8));
	}
	
	/**
	 * Tests for elder dwarf(level 20).
	 */
	@Test
	public void testElderDwarfSpeed() {
		Creature creature = new Creature();
		creature = SingletonRepository.getEntityManager().getCreature("elder dwarf");
		assertThat(creature.getBaseSpeed(), is(0.8));
	}
	
	/**
	 * Tests for hero dwarf(level 25).
	 */
	@Test
	public void testHeroDwarfSpeed() {
		Creature creature = new Creature();
		creature = SingletonRepository.getEntityManager().getCreature("hero dwarf");
		assertThat(creature.getBaseSpeed(), is(0.8));
	}
	
	/**
	 * Tests for duergar(level 29).
	 */
	@Test
	public void testDuergarSpeed() {
		Creature creature = new Creature();
		creature = SingletonRepository.getEntityManager().getCreature("duergar");
		assertThat(creature.getBaseSpeed(), is(0.8));
	}

}
