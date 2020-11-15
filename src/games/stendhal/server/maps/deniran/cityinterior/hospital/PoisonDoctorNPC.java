package games.stendhal.server.maps.deniran.cityinterior.hospital;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Map;

public class PoisonDoctorNPC implements ZoneConfigurator  {
	

	@Override
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Poison Doctor") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			public void createDialog() {
				addGreeting("Hi, do you need any #help");
				
				addGoodbye();
			}};
			
			npc.setPosition(19, 4);
			npc.setEntityClass("womanonstoolnpc");
			npc.setDescription("You see doctor. She will tell you a treatment.");
			zone.add(npc);
	}
}
