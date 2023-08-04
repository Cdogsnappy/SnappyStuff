package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CollectMission extends Mission {
    protected ItemStack toCollect;
    protected UUID playerID;
    protected boolean complete = false;


    public CollectMission(ItemStack toCollect){
        this.missionType = Type.COLLECT;
        this.toCollect = toCollect;
    }
    public CollectMission(ItemStack toCollect, UUID player, boolean complete){
        this.missionType = Type.COLLECT;
        this.toCollect = toCollect;
        this.playerID = player;
        this.complete = complete;
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
        toCollect.save(tag);
        tag.putUUID("id",playerID);
        tag.putBoolean("complete",complete);

        return tag;
    }

    public static CollectMission load(CompoundTag tag){
        ItemStack stack = ItemStack.of(tag);
        UUID player = tag.getUUID("id");
        boolean complete = tag.getBoolean("complete");
        return new CollectMission(stack,player,complete);
    }


    public boolean attemptComplete(Player player){
        if(player.getInventory().contains(toCollect)){
            player.getInventory().removeItem(toCollect);
            return complete = true;
        }
        return false;
    }


}
