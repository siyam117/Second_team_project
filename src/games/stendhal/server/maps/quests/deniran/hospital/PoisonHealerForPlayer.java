package games.stendhal.server.maps.quests.deniran.hospital;

import games.stendhal.server.entity.npc.*;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.player.*;
import games.stendhal.server.maps.Region;
import games.stendhal.server.maps.quests.AbstractQuest;

import java.util.*;

public class PoisonHealerForPlayer extends AbstractQuest {

    public static final String QUEST_SLOT = "healer_player";



    @Override
    public String getSlotName() {
        return QUEST_SLOT;
    }

    @Override
    public String getName() {
        return "PoisonHealerForPlayer";
    }

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I have talked to Poison Doctor.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("I do not want to waste my money.");
		}
		if (player.isQuestInState(QUEST_SLOT, "start", "done")) {
			res.add("I go to find Nurse.");
		}
		if ("start".equals(questState) && player.isEquipped("money")
				|| "done".equals(questState)) {
			res.add("I have enough money.");
		}
		if ("done".equals(questState)) {
			res.add("I gave the money to Nurse. She gave me medicine.");
		}
		return res;
	}
    
    private void prepareRequestingStep() {

        // get a reference to the Hayunn npc
        SpeakerNPC npc = npcs.get("Poison Doctor");
        
		npc.add(ConversationStates.ATTENDING,
				"help",
				null,
				ConversationStates.QUEST_OFFERED,
				"Sorry I'm busy right now, but if you need a healer, please go find a nurse",
				null);
		
		npc.add(
				ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"If you have other problem you can come back",
				new SetQuestAction(QUEST_SLOT, "start"));
		
		npc.add(
				ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"Oh, well, hope you will get well soon",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));
    }

    
    private void prepareBringingStep() {
        SpeakerNPC npc = npcs.get("Poison Nurse");
        
        // player has the quest active and has a beer with him, ask for it
        npc.add(
            ConversationStates.IDLE,
            ConversationPhrases.GREETING_MESSAGES,
            new AndCondition(new QuestActiveCondition(QUEST_SLOT), new PlayerHasItemWithHimCondition("money", 500)),
            ConversationStates.QUEST_ITEM_BROUGHT, 
            "Come here, I will give you medicine",
            null);

        // player has accepted the quest but did not bring a money, remind him
        npc.add(
            ConversationStates.IDLE,
            ConversationPhrases.GREETING_MESSAGES,
            new AndCondition(new QuestActiveCondition(QUEST_SLOT), new NotCondition(new PlayerHasItemWithHimCondition("money", 500))),
            ConversationStates.ATTENDING, 
            "You need to have 500 money, otherwise we are not able to help you",
            null);
        
        List<ChatAction> reward = new LinkedList<ChatAction>();
        reward.add(new DropItemAction("money", 500));
        reward.add(new SetQuestAction(QUEST_SLOT, "done"));
        reward.add(new EquipItemAction("poison healer"));
        npc.add(
    		 ConversationStates.QUEST_ITEM_BROUGHT,
    		 ConversationPhrases.YES_MESSAGES,
             new PlayerHasItemWithHimCondition("money", 500),
             ConversationStates.ATTENDING,
             "Take this medicine and have a rest for a while",
             new MultipleActions(reward));
        
		npc.add(
				ConversationStates.QUEST_ITEM_BROUGHT,
				ConversationPhrases.NO_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"You need to have 500 money",
				null);
        
    }
    
    @Override
    public String getNPCName() {
        return "Poison Doctor";
    }
    
	@Override
	public void addToWorld() {
		fillQuestInfo(
				"poison healer for player",
				"player with posioned status.",
				false);
		prepareRequestingStep();
		prepareBringingStep();
	}
	


	public String getTitle() {

		return "Poison Healer for Player";
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public String getRegion() {
		return Region.DENIRAN;
	}
}
