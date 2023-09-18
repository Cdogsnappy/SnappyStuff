package com.cdogsnappy.snappystuff.quest;

import com.cdogsnappy.snappystuff.network.PlayerQuestDataPacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class QuestData {

    public List<ItemStack> rewards;
    public List<ClosedContractQuest> acceptedQuests;
    public List<ClosedContractQuest> createdQuests;
    public QuestData(){
        rewards = Lists.newArrayList();
        acceptedQuests = Lists.newArrayList();
        createdQuests = Lists.newArrayList();
    }


    public CompoundTag save(CompoundTag tag){
        ListTag r = new ListTag();
        ListTag aq = new ListTag();
        ListTag cq = new ListTag();
        rewards.forEach((reward) -> {
            r.add(reward.save(new CompoundTag()));
        });
        acceptedQuests.forEach((accepted) -> {
            aq.add(accepted.save(new CompoundTag()));
        });
        createdQuests.forEach((created) -> {
            cq.add(created.save(new CompoundTag()));
        });
        tag.put("rewards",r);
        tag.put("accepted",aq);
        tag.put("created",cq);
        return tag;
    }
    public static QuestData load(CompoundTag tag){
        ListTag acceptedQuestsTag = (ListTag)tag.get("accepted");
        ListTag rewardRegistryTag = (ListTag)tag.get("rewards");
        ListTag createdQuestsTag = (ListTag)tag.get("created");
        QuestData res = new QuestData();
        for(int j = 0; j<acceptedQuestsTag.size(); ++j){
            ClosedContractQuest q = ClosedContractQuest.load(acceptedQuestsTag.getCompound(j));
            res.acceptedQuests.add(q);
            MissionHandler.loadMissions(q.missions,q.questor);
        }
        for(int i = 0; i<createdQuestsTag.size(); ++i){
            ClosedContractQuest q = ClosedContractQuest.load(createdQuestsTag.getCompound(i));
            res.createdQuests.add(q);
        }
        for(int k = 0; k < rewardRegistryTag.size(); ++k){
            res.rewards.add(ItemStack.of(rewardRegistryTag.getCompound(k)));
        }
        return res;
    }
    public void addReward(ItemStack stack) {
        for (ItemStack i : rewards) {
            if (i.sameItem(stack)) {
                int count = i.getCount();
                i.grow(Math.min(i.getMaxStackSize() - count, count+stack.getCount()));
                stack.shrink(Math.min(i.getMaxStackSize() - count, count+stack.getCount()));
                if(stack.isEmpty()){return;}
            }
        }
        rewards.add(stack);
    }

    /**
     * Adds a list of rewards into the rewardList, making sure to collapse similar item types into one entry.
     * @param rewardList
     */
    public void addAllRewards(Collection<ItemStack> rewardList) {
        for (ItemStack j : rewardList) {
            for (ItemStack i : rewards) {
                if (i.sameItem(j)) {
                    int count = i.getCount();
                    i.grow(Math.min(i.getMaxStackSize() - count,count+j.getCount()));
                    j.shrink(Math.min(j.getCount(),i.getMaxStackSize()-count));
                    break;
                }
            }
            if (j.getCount() != 0) {
                while(j.getCount() - j.getMaxStackSize() > 0){
                    rewards.add(new ItemStack(j.getItem(),j.getMaxStackSize()));
                    j.shrink(j.getMaxStackSize());
                }
                rewards.add(j);
            }
        }
    }

    /**
     * @author Cdogsnappy
     * Attempts to place rewards in the players inventory
     * @param p
     */
    public void disperseRewards(ServerPlayer p){
        Inventory inv = p.getInventory();
        for(int i = 0; i < rewards.size(); ++i){
            if(inv.add(rewards.get(i))){
                rewards.remove(i);
                --i;
            }
        }
        SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(p.getUUID()),p);//The client needs to update their rewardList
    }
}
