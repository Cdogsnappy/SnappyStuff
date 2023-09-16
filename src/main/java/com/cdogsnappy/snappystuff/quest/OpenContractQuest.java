package com.cdogsnappy.snappystuff.quest;

import com.cdogsnappy.snappystuff.quest.mission.PlayerKillMission;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpenContractQuest extends Quest{
    public PlayerKillMission mission;

    public OpenContractQuest(PlayerKillMission mission, List<ItemStack> rewards, UUID requestor, QuestType type){
        this.mission = mission;
        this.rewards = rewards;
        this.requestor = requestor;
        this.type = type;
        this.questID = UUID.randomUUID();
    }
    public OpenContractQuest(PlayerKillMission mission, List<ItemStack> rewards, UUID requestor, QuestType type, UUID id){
        this.mission = mission;
        this.rewards = rewards;
        this.requestor = requestor;
        this.type = type;
        this.questID = id;
    }
    public CompoundTag save(CompoundTag tag){
        ListTag rewards = new ListTag();
        for(int k = 0; k<this.rewards.size(); ++k){
            rewards.add(this.rewards.get(k).save(new CompoundTag()));
        }
        tag.putUUID("requestor", this.requestor);
        if(this.type == QuestType.PLAYER) {
            tag.putInt("type", 0);
        }
        else{
            tag.putInt("type", 1);
        }
        tag.put("mission", this.mission.save(new CompoundTag()));
        tag.put("rewards", rewards);
        tag.putUUID("id", this.questID);
        return tag;
    }
    public static OpenContractQuest load(CompoundTag tag) {
        List<ItemStack> rewards = new ArrayList<>();
        PlayerKillMission mission = PlayerKillMission.load(tag);
        QuestType type = switch(tag.getInt("type")){
            case 0 -> QuestType.PLAYER;
            case 1 -> QuestType.DAILY;
            default -> throw new IllegalStateException("Unexpected value: " + tag.getInt("type"));
        };
        ListTag rewardsTag = (ListTag)tag.get("rewards");
        for(int j = 0; j<rewardsTag.size(); ++j){
            rewards.add(ItemStack.of(rewardsTag.getCompound(j)));
        }
        return new OpenContractQuest(mission, rewards,tag.getUUID("requestor"), type, tag.getUUID("id"));
    }

    public void distributeRewards(Player murderer) {
        QuestHandler.playerQuestData.get(murderer).addAllRewards(this.rewards);
    }
}
