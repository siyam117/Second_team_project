package games.stendhal.server.entity.item;
	
import java.util.Map;
	
public class PoisonHealer extends Food {
	public PoisonHealer(final String name, final String clazz, final String subclass,
		final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}
	
	/**
	 * copy constructor.
	 *
	 * @param item
	 * item to copy
	 */
	public PoisonHealer(final PoisonHealer item) {
		super(item);
	}
}
