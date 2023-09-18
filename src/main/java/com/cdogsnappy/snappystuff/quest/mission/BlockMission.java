package com.cdogsnappy.snappystuff.quest.mission;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.Serializable;
import java.util.UUID;

public class BlockMission extends Mission implements Serializable {
    public Block toBreak;
    public int numToBreak;
    public int numBroken = 0;
    protected boolean complete;


    public BlockMission(Block block, int numToBreak){
        this.missionType = Type.BLOCK;
        this.toBreak = block;
        this.numToBreak = numToBreak;
        this.complete = false;

    }
    public BlockMission(Block block, int numToBreak, int numBroken, boolean complete){
        this.missionType = Type.BLOCK;
        this.toBreak = block;
        this.numToBreak = numToBreak;
        this.numBroken = numBroken;
        this.complete = complete;
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
    public CompoundTag save(CompoundTag tag){
        tag.putInt("type",1);
        tag.putString("block", Registry.BLOCK.getKey(toBreak).toString());
        tag.putInt("numToBreak", numToBreak);
        tag.putInt("numBroken", numBroken);
        tag.putBoolean("complete", complete);
        return tag;
    }
    public static BlockMission load(CompoundTag tag){
        Block toBreak = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(tag.getString("block")));
        int numToBreak = tag.getInt("numToBreak");
        int numBroken = tag.getInt("numBroken");
        boolean complete = tag.getBoolean("complete");
        return new BlockMission(toBreak,numToBreak,numBroken,complete);
    }

}
