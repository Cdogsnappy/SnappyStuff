package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CollectMission extends Mission {
    protected ItemStack toCollect;
    protected Player player;
    protected boolean complete = false;


    public CollectMission(ItemStack toCollect,Player questor){
        this.missionType = Type.COLLECT;
        this.toCollect = toCollect;
        this.player = questor;
    }

    @Override
    public boolean isComplete(){return complete;}

    @Override
    public boolean attemptComplete(){
        if(player.getInventory().contains(toCollect)){
            player.getInventory().removeItem(toCollect);
            return complete = true;
        }
        return false;
    }

}
