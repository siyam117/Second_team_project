package games.stendhal.server.maps.deniran.cityinterior.hospital;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Map;

public class ConfuseDoctorNPC implements ZoneConfigurator  {
	

	@Override
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Confuse Doctor") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			public void createDialog() {
				addGreeting("Hi, can I #help you?");
				

				addGoodbye();
			}};
			
			npc.setPosition(5, 14);
			npc.setEntityClass("woman_008_npc");
			npc.setDescription("You see a doctor. She will tell you a treatment.");
			zone.add(npc);
	}
}
