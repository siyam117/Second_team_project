package games.stendhal.server.entity.status;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import utilities.PlayerTestHelper;
import utilities.RPClass.CreatureTestHelper;
import utilities.RPClass.ItemTestHelper;

public class HeavyStatusAttackerTest {

	private Player player = PlayerTestHelper.createPlayer("bob");
	Player victim = PlayerTestHelper.createPlayer("victim");
	
	
	/**
	 * initialisation
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		MockStendlRPWorld.get();
		CreatureTestHelper.generateRPClasses();
		ItemTestHelper.generateRPClasses();
	}
	
	@AfterClass
	public static void removePlayerAfterClass() throws Exception{
		PlayerTestHelper.removePlayer("bob");
	}
	
	@Test
	public void testHeavyStatusAttacker() {
		
		final HeavyStatusAttacker attacker = new HeavyStatusAttacker(100);
		attacker.onHit(victim, player, 0);
		assertTrue(victim.hasStatus(StatusType.HEAVY));
	}

	@Test
	public void testHeavyStatusAttackerProbabilityZero() {
		
		final HeavyStatusAttacker attacker = new HeavyStatusAttacker(0);
		attacker.onAttackAttempt(victim, player);
		assertFalse(victim.hasStatus(StatusType.HEAVY));
	}
	
}