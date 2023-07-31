package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.io.Serializable;
import java.util.UUID;

public class BlockMission extends Mission implements Serializable {
    protected Block toBreak;
    protected UUID player;
    protected int numToBreak;
    protected int numBroken = 0;
    protected boolean complete = false;


    public BlockMission(Block block,UUID questor, int numToBreak){
        this.missionType = Type.BLOCK;
        this.toBreak = block;
        this.player = questor;
        this.numToBreak = numToBreak;

    }

    @Override
    public boolean isComplete(){return complete;}

    @Override
    public boolean attemptComplete(){
        if(numBroken>=numToBreak){
            return complete = true;
        }
        return false;
    }

}
