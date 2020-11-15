package games.stendhal.server.maps.deniran;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;

import java.util.Arrays;
import java.util.Map;

public class ReceptionistNPC implements ZoneConfigurator  {
	
	private final ShopList shops = SingletonRepository.getShopList();

	@Override
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Receptionist") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			public void createDialog() {
				addGreeting("Hello and welcome to Deniran Hospital. I will sell #fruits. #help for more");
				addHelp("We will open soon, if you interesting in this, contact us to be a admin to test");
				
				new SellerAdder().addSeller(this, new SellerBehaviour(shops.get("deniranhospitalreception")));

				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("fruit"),
				        null,
				        ConversationStates.ATTENDING,
				        "We only offer #apple, #carrot now, future will have more fruits and flowers and get well soon card",
				        null);

				
				

				addGoodbye();
			}};
			
			npc.setPosition(54, 10);
			npc.setEntityClass("woman_001_npc");
			npc.setDescription("You see receptionist. She sells something helpful.");
			zone.add(npc);
	}
}
