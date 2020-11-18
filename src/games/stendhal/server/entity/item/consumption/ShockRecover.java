package games.stendhal.server.entity.item.consumption;	
	
import games.stendhal.server.entity.item.ConsumableItem;	
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.status.EatStatus;
import games.stendhal.server.entity.status.ShockRecoveringStatus;

public class ShockRecover implements Feeder{
	
	@Override
	
	public boolean feed(final ConsumableItem item, final Player player) {
		
		// Make player healed when recovering, in this stage use EatStatus
		ConsumableItem splitOff = (ConsumableItem) item.splitOff(1);
		EatStatus status = new EatStatus(splitOff.getAmount(), splitOff.getFrecuency(), splitOff.getRegen());
		player.getStatusList().inflictStatus(status, splitOff);
	
		// Make player unable to move when they are recovering
		ShockRecoveringStatus shock_recoveringstatus = new ShockRecoveringStatus();
		shock_recoveringstatus.setStepsDelay(0);
		player.getStatusList().inflictStatus(shock_recoveringstatus, splitOff);
	
		return true;
	}
}