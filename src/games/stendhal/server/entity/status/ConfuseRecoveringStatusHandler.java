package games.stendhal.server.entity.status;

import games.stendhal.common.NotificationType;
import games.stendhal.server.core.events.TurnNotifier;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.RPEntity;

public class ConfuseRecoveringStatusHandler implements StatusHandler <ConfuseRecoveringStatus>{


	/**
	 * inflicts a status
	 *
	 * @param status Status to inflict
	 * @param statusList StatusList
	 * @param attacker the attacker
	 */
	@Override
	public void inflict(ConfuseRecoveringStatus status, StatusList statusList, Entity attacker) {
		if (!statusList.hasStatus(status.getStatusType())) {
			RPEntity entity = statusList.getEntity();
			if (entity != null) {
				if (attacker == null) {
					entity.sendPrivateText(NotificationType.SCENE_SETTING, "You are healing.");
				} else {
					entity.sendPrivateText(NotificationType.SCENE_SETTING, "You are healing by " + attacker.getName() + ", will take 25s.");     
				}
			}
		}
		int count = statusList.countStatusByType(status.getStatusType());
		if (count <= 6) {
			statusList.addInternal(status);
		}

		if (count == 0) {
			statusList.activateStatusAttribute("status_" + status.getName());
			TurnNotifier.get().notifyInSeconds(25, new StatusRemover(statusList, status));
			TurnNotifier.get().notifyInTurns(0, new ShockRecoveringStatusTurnListener(statusList));
		}
	}

	/**
	 * removes a status
	 *
	 * @param status Status to inflict
	 * @param statusList StatusList
	 */
	@Override
	public void remove(ConfuseRecoveringStatus status, StatusList statusList) {
		statusList.removeInternal(status);

		RPEntity entity = statusList.getEntity();
		if (entity == null) {
			return;
		}

		Status nextStatus = statusList.getFirstStatusByClass(ConfuseRecoveringStatus.class);
		if (nextStatus != null) {
			TurnNotifier.get().notifyInSeconds(60, new StatusRemover(statusList, nextStatus));
		} else {
			entity.sendPrivateText(NotificationType.SCENE_SETTING, "Congratulations, you have been healed and immunize from confuse for a while.");
			entity.remove("status_" + status.getName());
			if (entity.has("status_confuse")) {
				entity.remove("status_confuse");
				entity.getStatusList().setImmune(StatusType.CONFUSED);
			}
		}
	}
}
