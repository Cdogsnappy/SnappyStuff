package com.cdogsnappy.snappystuff.quest;


import com.cdogsnappy.snappystuff.network.PlayerQuestDataPacket;
import com.cdogsnappy.snappystuff.network.QuestRequestPacket;
import com.cdogsnappy.snappystuff.network.QuestScreenPacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.quest.mission.CollectMission;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.ServerLifecycleHooks;

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
            unacceptedQuestsTag.add(q.save(new CompoundTag()));
        });
        openContractQuests.forEach((q) -> {
            openContractQuestsTag.add(q.save(new CompoundTag()));
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

    public static void acceptQuest(ClosedContractQuest q, ServerPlayer player){
        for(ClosedContractQuest c : unacceptedQuests){
            if(c.questID.equals(q.questID)){
                int ind = unacceptedQuests.indexOf(c);
                unacceptedQuests.remove(c);
                if(unacceptedQuests.size() == 0){SnappyNetwork.sendToPlayer(new QuestScreenPacket((Quest)null), player);}
                else if(ind > unacceptedQuests.size() && ind != 0){
                    SnappyNetwork.sendToPlayer(new QuestScreenPacket(unacceptedQuests.get(ind-1)),player);
                }
                else{
                    SnappyNetwork.sendToPlayer(new QuestScreenPacket(unacceptedQuests.get(ind)),player);
                }
                q.questor = player.getUUID();
                playerQuestData.get(player.getUUID()).acceptedQuests.add(q);
                List<ClosedContractQuest> requestorQuests = playerQuestData.get(q.requestor).createdQuests;
                for(ClosedContractQuest j : requestorQuests){
                    if(q.questID.equals(j.questID)){
                        requestorQuests.remove(j);
                        requestorQuests.add(q);
                        break;
                    }
                }
                MissionHandler.loadMissions(q.missions,player.getUUID());
                ServerPlayer p = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(q.requestor);
                if(!p.equals(null)){SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(q.questor),p);}
                return;
            }
        }
    }
    public static void removeQuest(ClosedContractQuest cq, ServerPlayer sender){
        if(cq.questor != null){
            playerQuestData.get(cq.questor).acceptedQuests.remove(cq);
            SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(cq.questor),sender);
        }
        else{unacceptedQuests.remove(cq);}
        playerQuestData.get(cq.requestor).createdQuests.remove(cq);
        playerQuestData.get(cq.requestor).addAllRewards(cq.rewards);
        ServerPlayer sp = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(cq.requestor);
        if(sp != null){
            SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(cq.requestor),sp);
        }
    }
    public static void attemptCompleteQuest(ClosedContractQuest q, ServerPlayer player){
        for(Mission m : q.missions){//Annoying, we have to run through all of the collection quests before we check for completion, otherwise it will only update the first collect mission's completion.
            if(m.missionType == Mission.Type.COLLECT){
                ((CollectMission)m).attemptComplete(player);
            }
        }
        for(Mission m : q.missions){
            if(!m.isComplete()){
                SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(q.questor),player);//Update the collection count on collect missions
                ServerPlayer p;
                if((p = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(q.requestor)) != null){
                    SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(q.requestor),p);//This covers the rare case where the quest GIVER is online and looking at their questbook
                }
                return;
            }
            }//Cases above are the only ones that could lead to a quest completion, anything else is an incomplete quest

        QuestHandler.playerQuestData.get(q.questor).acceptedQuests.remove(q);
        QuestHandler.playerQuestData.get(q.requestor).createdQuests.remove(q);
        q.distributeRewards();
        SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(q.questor),player);//Update the collection count on collect missions
        ServerPlayer p;
        if((p = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(q.requestor)) != null){
            SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(q.requestor),p);//This covers the rare case where the quest GIVER is online and looking at their questbook
        }
        }


}
