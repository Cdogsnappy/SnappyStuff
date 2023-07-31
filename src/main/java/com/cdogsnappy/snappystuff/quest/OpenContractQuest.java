package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OpenContractQuest extends Quest{
    protected PlayerKillMission mission;

    public OpenContractQuest(PlayerKillMission mission, List<ItemStack> rewards, UUID requestor){
        this.mission = mission;
        this.rewards = rewards;
        this.requestor = requestor;
    }

    public static CompoundTag save(CompoundTag tag, OpenContractQuest q){
        ListTag rewards = new ListTag();
        for(int k = 0; k<q.rewards.size(); ++k){
            rewards.add(q.rewards.get(k).save(new CompoundTag()));
        }
        tag.putUUID("requestor", q.requestor);
        tag.put("mission", q.mission.save(new CompoundTag()));
        tag.put("rewards", rewards);
        return tag;
    }
    public static void load(CompoundTag tag) {
        UUID requestor = tag.getUUID("requestor");
        List<ItemStack> rewards = new ArrayList<>();
        PlayerKillMission mission = (PlayerKillMission)PlayerKillMission.load(tag);
        ListTag rewardsTag = (ListTag)tag.get("rewards");
        for(int j = 0; j<rewardsTag.size(); ++j){
            rewards.add(ItemStack.of(rewardsTag.getCompound(j)));
        }

            new OpenContractQuest(mission, rewards, requestor);

    }

    public void distributeRewards(Player murderer) {
        for(int j = 0; j<this.rewards.size(); ++j){
            murderer.getInventory().add(this.rewards.get(j));
        }
    }
}
