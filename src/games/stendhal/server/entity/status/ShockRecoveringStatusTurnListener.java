package games.stendhal.server.entity.status;
	
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.core.events.TurnNotifier;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;
	
public class ShockRecoveringStatusTurnListener implements TurnListener {
	
	private StatusList statusList;

	/**
	 * ShockRecoveringStatusTurnListener
	 *
	 * @param statusList StatusList
	 */
	public ShockRecoveringStatusTurnListener(StatusList statusList) {
		this.statusList = statusList;
	}
	
	@Override
	public void onTurnReached(int currentTurn) {
		RPEntity entity = statusList.getEntity();
		ShockRecoveringStatus status = statusList.getFirstStatusByClass(ShockRecoveringStatus.class);
	
		// check that the entity exists and has this status
		if ((entity == null) || (status == null)) {
			return;
		}
		// Stop the entity's movement after n steps
		int stepsTaken = entity.getStepsTaken();
		if (stepsTaken >= status.getStepsDelay()) {
			if (entity instanceof Player) {
				((Player) entity).forceStop();
			} else {
				entity.stop();
			}
			entity.clearPath();
		}
		TurnNotifier.get().notifyInTurns(0, this);
	}
}