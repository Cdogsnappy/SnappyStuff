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
    public List<Quest> quests = new ArrayList<>();
    UUID player;
    protected List<IMission> missions;
    protected List<ItemStack> reward;

    public Quest(List<IMission> missions, List<ItemStack> rewards){
        this.missions = missions;
        this.reward = rewards;
        quests.add(this);

    }
    public Quest(List<IMission> missions, List<ItemStack> rewards, UUID player){
        this.missions = missions;
        this.reward = rewards;
        this.player = player;
        quests.add(this);

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
    public ListTag save(ListTag list){
        for(Quest quest : quests){
            CompoundTag tag = new CompoundTag();
            ListTag missions = new ListTag();
            ListTag rewards = new ListTag();
            for(IMission mission : quest.missions){
                missions.add(mission.save(new CompoundTag()));
            }
            for(ItemStack i : quest.reward){
                rewards.add(i.save(new CompoundTag()));
            }
            tag.putUUID("id", player);
            tag.put("missions",missions);
            tag.put("rewards",rewards);
            list.add(tag);
        }
        return list;
    }
    public void load(ListTag list){
        for(int i = 0; i<list.size(); i++){
            CompoundTag tag = list.getCompound(i);
            UUID player = tag.getUUID("id");
            List<IMission> missions = new ArrayList<>();
            List<ItemStack> rewards = new ArrayList<>();
            ListTag missionTag = (ListTag)tag.get("missions");
            ListTag rewardsTag = (ListTag)tag.get("rewards");
            for(Tag t : missionTag){
                missions.add(Mission.load((CompoundTag)t));
            }
            for(Tag t : rewardsTag){
                rewards.add(ItemStack.of((CompoundTag)t));
            }
            new Quest(missions,rewards,player);
        }
    }


}
