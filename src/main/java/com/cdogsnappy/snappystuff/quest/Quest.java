package com.cdogsnappy.snappystuff.quest;

import com.mojang.serialization.JsonOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quest {
    UUID player;
    protected List<IMission> missions;
    protected List<ItemStack> reward;

    public Quest(List<IMission> missions, List<ItemStack> rewards){
        this.missions = missions;
        this.reward = rewards;
        this.player = null;
        QuestHandler.quests.add(this);

    }
    public Quest(List<IMission> missions, List<ItemStack> rewards, UUID player){
        this.missions = missions;
        this.reward = rewards;
        this.player = player;
        QuestHandler.quests.add(this);

    }
    public void acceptQuest(Player player){
        this.player = player.getUUID();
    }
    public boolean isComplete(){
        for(IMission mission : missions){
            if(!mission.isComplete()){
                return false;
            }
        }
        return true;
    }
    public static ListTag save(ListTag list, Quest q){
            CompoundTag tag = new CompoundTag();
            ListTag missions = new ListTag();
            ListTag rewards = new ListTag();
            for(int j = 0; j<q.missions.size(); ++j){
                missions.add(q.missions.get(j).save(new CompoundTag()));
            }
            for(int k = 0; k<q.reward.size(); ++k){
                rewards.add(q.reward.get(k).save(new CompoundTag()));
            }
            tag.putUUID("id", q.player);
            tag.put("missions", missions);
            tag.put("rewards", rewards);
            list.add(tag);
            return list;
    }
    public static void load(CompoundTag tag){
        UUID player = tag.getUUID("id");
        List<IMission> missions = new ArrayList<>();
        List<ItemStack> rewards = new ArrayList<>();
        ListTag missionTag = (ListTag)tag.get("missions");
        ListTag rewardsTag = (ListTag)tag.get("rewards");
        for(int k = 0; k<missionTag.size(); ++k){
            missions.add(Mission.load(missionTag.getCompound(k)));
        }
        for(int j = 0; j<rewardsTag.size(); ++j){
            rewards.add(ItemStack.of(rewardsTag.getCompound(j)));
        }
        new Quest(missions,rewards,player);
    }


}
