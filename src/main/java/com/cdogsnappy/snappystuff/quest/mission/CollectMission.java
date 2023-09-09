package com.cdogsnappy.snappystuff.quest.mission;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class CollectMission extends Mission {
    public ItemStack toCollect;
    public UUID requestor;
    public int numToCollect;
    public int numCollected;
    protected boolean complete = false;


    public CollectMission(ItemStack toCollect, UUID requestor){
        this.missionType = Type.COLLECT;
        this.toCollect = new ItemStack(toCollect.getItem());
        numToCollect = toCollect.getCount();
        numCollected = 0;
        this.requestor = requestor;
    }
    public CollectMission(ItemStack toCollect, int numToCollect, int numCollected, UUID requestor, boolean complete){
        this.missionType = Type.COLLECT;
        this.toCollect = new ItemStack(toCollect.getItem());
        this.numToCollect = numToCollect;
        this.numCollected = numCollected;
        this.complete = complete;
        this.requestor = requestor;
    }

    public CollectMission(ItemStack itemStack, int i, UUID requestor) {
        this.missionType = Type.COLLECT;
        this.toCollect = itemStack;
        this.numToCollect = i;
        this.numCollected = 0;
        this.requestor = requestor;
    }

    @Override
    public boolean isComplete(){return complete;}

    /**
     * @author Cdogsnappy
     * save the mission to nbt data
     * @param tag the tag to save the data to
     * @return the tag with the data
     */
    @Override
    public CompoundTag save(CompoundTag tag){
        tag.putInt("type",2); //FOR MISSION RECOVERY PURPOSES
        tag.putInt("toCollect",numToCollect);
        tag.putInt("collected",numCollected);
        tag.putUUID("requestor",requestor);
        toCollect.save(tag);
        tag.putBoolean("complete",complete);

        return tag;
    }

    public static CollectMission load(CompoundTag tag){
        ItemStack stack = ItemStack.of(tag);
        boolean complete = tag.getBoolean("complete");
        return new CollectMission(stack,tag.getInt("toCollect"),tag.getInt("collected"), tag.getUUID("requestor"),complete);
    }

    public boolean attemptComplete(Player player){
        int numRemaining[] = new int[1];
        numRemaining[0] = this.numToCollect - numCollected;
        Inventory inv = player.getInventory();
        inv.items.forEach((i) -> {
            if(i.sameItem(this.toCollect)){
                int takeAway = Math.min(numRemaining[0],i.getCount());
                if(takeAway == i.getCount()){inv.removeItem(i);}
                else{i.setCount(takeAway);}
                numRemaining[0]-=takeAway;
            }
        });
        List<ItemStack> currRewards = QuestHandler.playerQuestData.get(requestor).rewards;
        currRewards.add(new ItemStack(toCollect.getItem(),(numToCollect - numCollected - numRemaining[0])));
        if(numRemaining[0] == 0){return complete=true;}
        return false;
    }
}
