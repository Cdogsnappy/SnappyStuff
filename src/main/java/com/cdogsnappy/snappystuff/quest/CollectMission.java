package com.cdogsnappy.snappystuff.quest;

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


    public boolean attemptComplete(Player player){
        if(player.getInventory().contains(toCollect)){
            player.getInventory().removeItem(toCollect);
            return complete = true;
        }
        return false;
    }

}
