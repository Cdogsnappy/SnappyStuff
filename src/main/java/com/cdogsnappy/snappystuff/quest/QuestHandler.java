package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class QuestHandler extends SavedData {

    public static List<ClosedContractQuest> unacceptedQuests = new ArrayList<>();
    public static List<OpenContractQuest> openContractQuests = new ArrayList<>();
    public static HashMap<UUID, List<ClosedContractQuest>> acceptedQuests = new HashMap<>();
    public static QuestHandler q;
    public static LocalDateTime dailiesTime = LocalDateTime.now().minusDays(2);//If this isn't overwritten on server start then no dailies are active.

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
        acceptedQuests.forEach((key,val) ->{
            for(int j = 0; j<val.size(); ++j){
                acceptedQuestsTag.add(ClosedContractQuest.save(new CompoundTag(),val.get(j)));
            }
        });
        for(int i = 0; i < unacceptedQuests.size(); ++i){
            unacceptedQuestsTag.add(ClosedContractQuest.save(new CompoundTag(), unacceptedQuests.get(i)));
        }
        for(int k = 0; k < openContractQuests.size(); ++k){
            openContractQuestsTag.add(OpenContractQuest.save(new CompoundTag(),openContractQuests.get(k)));
        }
        tag.put("acceptedquests",acceptedQuestsTag);
        tag.put("unacceptedquests",unacceptedQuestsTag);
        tag.put("opencontractquests",openContractQuestsTag);
        return tag;
    }

    /**
     * @author Cdogsnappy
     * Loads the questlog data from the overworld and puts it back into the Quest Handling system
     * @param tag the tag with the questlog
     * @return the QuestHandler dummy instance
     */
    public static QuestHandler load(CompoundTag tag){
        ListTag questsTag = (ListTag)tag.get("acceptedquests");
        ListTag unacceptedQuestsTag = (ListTag)tag.get("unacceptedquests");
        ListTag openContractQuestsTag = (ListTag)tag.get("opencontractquests");
        for(int i = 0; i<unacceptedQuestsTag.size(); ++i){
           unacceptedQuests.add(ClosedContractQuest.load(questsTag.getCompound(i),true));
        }
        for(int j = 0; j<questsTag.size(); ++j){
            ClosedContractQuest q = ClosedContractQuest.load(unacceptedQuestsTag.getCompound(j),false);
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
        for(int k = 0; k<openContractQuestsTag.size(); ++k){
            openContractQuests.add(OpenContractQuest.load(openContractQuestsTag.getCompound(k)));
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
            if(openContractQuests.get(j).mission.toKill == murdered.getUUID()){
                openContractQuests.remove(j).distributeRewards(murderer);
            }
        }

    }

}
