package com.cdogsnappy.snappystuff.quest;

import com.cdogsnappy.snappystuff.quest.mission.DailyMission;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class QuestHandler extends SavedData {
    /**
     * This data is only kept up to date on the server side, if you need to access it on the client side
     * for any reason you'll have to ask the server through a packet.
     */

    public static List<ClosedContractQuest> unacceptedQuests = new ArrayList<>();
    public static List<OpenContractQuest> openContractQuests = new ArrayList<>();
    public static HashMap<UUID, List<ClosedContractQuest>> acceptedQuests = new HashMap<>();
    public static QuestHandler q;
    public static HashMap<UUID,List<ItemStack>> rewardRegistry = new HashMap<>();

    /**
     * @author Cdogsnappy
     * retrieves the questlog data from the level
     * @param level the level to get the data from (OVERWORLD)
     * @return the dummy QuestHandler instance
     */
    public static QuestHandler get(Level level){
        if(level.isClientSide){
            throw new RuntimeException("CANNOT SAVE GLOBAL DATA ON CLIENT SIDE");
        }
        DimensionDataStorage dataStorage = ((ServerLevel)level).getDataStorage();
        return q =  dataStorage.computeIfAbsent(QuestHandler::load,QuestHandler::new,"questhandler");

    }

    /**
     * Called if no questlog data is found, creates empty questlog
     */
    public QuestHandler(){q = this;}

    /**
     * Writes the entire global questlog to NBT data and saves it to the overworld data
     * @param tag the tag to save the questlog to
     * @return the tag with the questlog saved to it
     */
    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag acceptedQuestsTag = new ListTag();
        ListTag unacceptedQuestsTag = new ListTag();
        ListTag openContractQuestsTag = new ListTag();
        ListTag rewardRegistryTag = new ListTag();
        acceptedQuests.forEach((key,val) ->{
            for(ClosedContractQuest q : val){
                acceptedQuestsTag.add(ClosedContractQuest.save(new CompoundTag(),q));
            }
        });
        unacceptedQuests.forEach((q) -> {
            unacceptedQuestsTag.add(ClosedContractQuest.save(new CompoundTag(), q));
        });
        openContractQuests.forEach((q) -> {
            openContractQuestsTag.add(OpenContractQuest.save(new CompoundTag(),q));
        });
        rewardRegistry.forEach((k,v) -> {
            CompoundTag playerRewards = new CompoundTag();
            playerRewards.putUUID("player",k);
            ListTag rewards = new ListTag();
            v.forEach((r) ->{
                rewards.add(r.save(new CompoundTag()));
            });
            playerRewards.put("rewards",rewards);
            rewardRegistryTag.add(playerRewards);
        });
        tag.put("acceptedquests",acceptedQuestsTag);
        tag.put("unacceptedquests",unacceptedQuestsTag);
        tag.put("opencontractquests",openContractQuestsTag);
        tag.put("rewardregistry",rewardRegistryTag);
        return tag;
    }

    /**
     * @author Cdogsnappy
     * Loads the questlog data from the overworld and puts it back into the Quest Handling system
     * @param tag the tag with the questlog
     * @return the QuestHandler dummy instance
     */
    public static QuestHandler load(CompoundTag tag){
        ListTag acceptedQuestsTag = (ListTag)tag.get("acceptedquests");
        ListTag unacceptedQuestsTag = (ListTag)tag.get("unacceptedquests");
        ListTag openContractQuestsTag = (ListTag)tag.get("opencontractquests");
        ListTag rewardRegistryTag = (ListTag)tag.get("rewardregistry");
         //unaccepted quests load
        for(int i = 0; i<unacceptedQuestsTag.size(); ++i){
           unacceptedQuests.add(ClosedContractQuest.load(unacceptedQuestsTag.getCompound(i),false));
        }
        //accepted quests load
        for(int j = 0; j<acceptedQuestsTag.size(); ++j){
            ClosedContractQuest q = ClosedContractQuest.load(acceptedQuestsTag.getCompound(j),true);
            List<ClosedContractQuest> list;
            if(acceptedQuests.containsKey(q.questor)){
                list = acceptedQuests.get(q.questor);
            }
            else{
                list = new ArrayList<>();
            }
            list.add(q);
            acceptedQuests.put(q.questor,list);
        }
        //open contracts load
        for(int k = 0; k<openContractQuestsTag.size(); ++k){
            openContractQuests.add(OpenContractQuest.load(openContractQuestsTag.getCompound(k)));
        }
        //player rewards load
        for(int l = 0; l<rewardRegistryTag.size(); ++l){
            CompoundTag playerTag = rewardRegistryTag.getCompound(l);
            ListTag rewards = (ListTag)playerTag.get("rewards");
            List<ItemStack> items = new ArrayList<>();
            for(int x = 0; x<rewards.size(); ++x){
                items.add(ItemStack.of(rewards.getCompound(x)));
            }
            rewardRegistry.put(playerTag.getUUID("player"),items);
        }
        return new QuestHandler();
    }
    /**
     * @author Cdogsnappy
     * called on server death to tell the server to save the questlog
     */
    public static void setQuestsDirty(){
        q.setDirty();
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
