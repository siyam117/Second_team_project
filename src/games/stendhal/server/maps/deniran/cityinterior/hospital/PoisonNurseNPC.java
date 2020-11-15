package games.stendhal.server.maps.deniran.cityinterior.hospital;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Map;

public class PoisonNurseNPC implements ZoneConfigurator  {
	

	@Override
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Poison Nurse") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			public void createDialog() {
				addGreeting("Hi, I can give you medicine for poison stauts, but need allowance from doctor");
				
				

				addGoodbye();
			}};
			
			npc.setPosition(27, 2);
			npc.setEntityClass("woman_005_npc");
			npc.setDescription("You see nurese. She will tell you medicine.");
			zone.add(npc);
	}
}
