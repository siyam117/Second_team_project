package games.stendhal.server.entity.status;

public class ShockRecoveringStatus extends Status {
	public ShockRecoveringStatus() {
		super("shock_recovering");
	}
	
	// Entity will stop after STEPS_DELAY steps
	private int STEPS_DELAY = 5;
	
	/**
	 * returns the status type
	 *
	 * @return StatusType
	 */
	
	@Override
	public StatusType getStatusType() {
		return StatusType.SHOCKRECOVERING;
	}
	
	public int getStepsDelay() {
		return STEPS_DELAY;
	}
	
	public void setStepsDelay(int delay) {
		this.STEPS_DELAY = delay;
	}

}