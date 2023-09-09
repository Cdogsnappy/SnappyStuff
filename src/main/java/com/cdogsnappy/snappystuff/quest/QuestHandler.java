package com.cdogsnappy.snappystuff.quest;


import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class QuestHandler{
    /**
     * This data is only kept up to date on the server side, if you need to access it on the client side
     * for any reason you'll have to ask the server through a packet.
     */

    public static List<ClosedContractQuest> unacceptedQuests = new ArrayList<>();
    public static List<OpenContractQuest> openContractQuests = new ArrayList<>();
    public static HashMap<UUID,QuestData> playerQuestData = new HashMap<>();
    /**
     * Writes the entire global questlog to NBT data and saves it to the overworld data
     * @param tag the tag to save the questlog to
     * @return the tag with the questlog saved to it
     */
    public static CompoundTag save(CompoundTag tag) {
        ListTag unacceptedQuestsTag = new ListTag();
        ListTag openContractQuestsTag = new ListTag();
        ListTag questDataTag = new ListTag();
        unacceptedQuests.forEach((q) -> {
            unacceptedQuestsTag.add(ClosedContractQuest.save(new CompoundTag(), q));
        });
        openContractQuests.forEach((q) -> {
            openContractQuestsTag.add(OpenContractQuest.save(new CompoundTag(),q));
        });
        playerQuestData.forEach((k,v) -> {
            CompoundTag pData = new CompoundTag();
            pData.putUUID("player",k);
            pData.put("data",v.save(new CompoundTag()));
            questDataTag.add(pData);
        });
        tag.put("unacceptedquests",unacceptedQuestsTag);
        tag.put("opencontractquests",openContractQuestsTag);
        tag.put("playerData",questDataTag);
        return tag;
    }

    /**
     * @author Cdogsnappy
     * Loads the questlog data from the overworld and puts it back into the Quest Handling system
     * @param tag the tag with the questlog
     * @return the QuestHandler dummy instance
     */
    public static void load(CompoundTag tag){
        ListTag unacceptedQuestsTag = (ListTag)tag.get("unacceptedquests");
        ListTag openContractQuestsTag = (ListTag)tag.get("opencontractquests");
        ListTag questDataTag = (ListTag)tag.get("playerData");
         //unaccepted quests load
        for(int i = 0; i<unacceptedQuestsTag.size(); ++i){
           unacceptedQuests.add(ClosedContractQuest.load(unacceptedQuestsTag.getCompound(i)));
        }
        //open contracts load
        for(int k = 0; k<openContractQuestsTag.size(); ++k){
            openContractQuests.add(OpenContractQuest.load(openContractQuestsTag.getCompound(k)));
        }
        for(int j = 0; j<questDataTag.size(); ++j){
            playerQuestData.put(questDataTag.getCompound(j).getUUID("player"),QuestData.load(questDataTag.getCompound(j).getCompound("data")));
        }
    }
    /**
     * @author Cdogsnappy
     * Handles all open contract quests, no client side handling required, once the quest is completed it will remove itself and
     * distribute rewards to the player
     * @param murdered player that was killed
     * @param murderer player that killed
     */
    public static void onMurder(Player murdered, Player murderer){
        for(int j = 0; j<openContractQuests.size(); ++j){
            if(openContractQuests.get(j).mission.getTarget() == murdered.getUUID()){
                openContractQuests.remove(j).distributeRewards(murderer);
            }
        }

    }

}
