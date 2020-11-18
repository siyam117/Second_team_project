package games.stendhal.server.entity.status;

public class HeavyStatusAttacker extends StatusAttacker{

	public HeavyStatusAttacker(double probability) {
		super(new HeavyStatus(), probability);
	}
}