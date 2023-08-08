package com.cdogsnappy.snappystuff.quest;

import com.cdogsnappy.snappystuff.quest.mission.IMission;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClosedContractQuest extends Quest{

    protected UUID questor;//Player who has accepted quest
    protected List<Mission> missions;
    public ClosedContractQuest(List<Mission> missions, List<ItemStack> rewards, UUID requestor, QuestType type){
        this.missions = missions;
        this.rewards = rewards;
        this.questor = null;
        this.requestor = requestor;
        this.type = type;


    }
    public ClosedContractQuest(List<Mission> missions, List<ItemStack> rewards, UUID requestor, UUID questor, QuestType type){
        this.missions = missions;
        this.rewards = rewards;
        this.questor = questor;
        this.requestor = requestor;
        this.type = type;


    }
    public void acceptQuest(Player player){
        this.questor = player.getUUID();
        QuestHandler.unacceptedQuests.remove(this);
        List<ClosedContractQuest> quests;
        if(QuestHandler.acceptedQuests.containsKey(player.getUUID())){
            quests = QuestHandler.acceptedQuests.get(this.questor);
            quests.add(this);
        }
        else{
            quests = new ArrayList<>();
            quests.add(this);
        }
        QuestHandler.acceptedQuests.put(this.questor, quests);
    }
    public boolean isComplete(){
        for(IMission mission : missions){
            if(!mission.isComplete()){
                return false;
            }
        }
        return true;
    }

    /**
     * @author Cdogsnappy
     * Saves converts a quest into NBT data and returns it
     * @param tag the tag to save the quest to
     * @param q the quest to save
     * @return the tag with the quest saved to it
     */
    public static CompoundTag save(CompoundTag tag, ClosedContractQuest q){

        ListTag missions = new ListTag();
        ListTag rewards = new ListTag();
        for(int j = 0; j<q.missions.size(); ++j){
            missions.add(q.missions.get(j).save(new CompoundTag()));
        }
        for(int k = 0; k<q.rewards.size(); ++k){
            rewards.add(q.rewards.get(k).save(new CompoundTag()));
        }
        if(q.questor != null) {
            tag.putUUID("questor", q.questor);
        }
        if(q.type == QuestType.PLAYER) {
            tag.putInt("type", 0);
        }
        else{
            tag.putInt("type", 1);
        }
            tag.putUUID("requestor", q.requestor);
            tag.put("missions", missions);
            tag.put("rewards", rewards);
            return tag;
    }

    /**
     * @author Cdogsnappy
     * Given a tag with a quest saved to it, rebuild the quest
     * @param tag the tag with the quest on it
     */
    public static ClosedContractQuest load(CompoundTag tag, boolean accepted) {

        UUID requestor = tag.getUUID("requestor");
        List<Mission> missions = new ArrayList<>();
        List<ItemStack> rewards = new ArrayList<>();
        ListTag missionTag = (ListTag)tag.get("missions");
        ListTag rewardsTag = (ListTag)tag.get("rewards");
        for(int k = 0; k<missionTag.size(); ++k){
            missions.add(Mission.load(missionTag.getCompound(k)));
        }
        for(int j = 0; j<rewardsTag.size(); ++j){
            rewards.add(ItemStack.of(rewardsTag.getCompound(j)));
        }
        QuestType type = switch(tag.getInt("type")){
            case 0 -> QuestType.PLAYER;
            case 1 -> QuestType.DAILY;
            default -> throw new IllegalStateException("Unexpected value: " + tag.getInt("type"));
        };
        if(accepted) {
            UUID player = tag.getUUID("questor");
            MissionHandler.loadMissions(missions, player);
            return new ClosedContractQuest(missions, rewards, requestor, player, type);
        }
        else{
            return new ClosedContractQuest(missions, rewards, requestor, type);
        }
    }


}