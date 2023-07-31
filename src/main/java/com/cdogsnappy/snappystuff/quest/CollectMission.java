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

    @Override
    public boolean isComplete(){return complete;}

    @Override
    public CompoundTag save(CompoundTag tag){
        toCollect.save(tag);
        tag.putString("id",playerID.toString());
        tag.putBoolean("complete",complete);

        return tag;
    }

    public static IMission load(CompoundTag tag){
        return null;
    }


    public boolean attemptComplete(Player player){
        if(player.getInventory().contains(toCollect)){
            player.getInventory().removeItem(toCollect);
            return complete = true;
        }
        return false;
    }


}
