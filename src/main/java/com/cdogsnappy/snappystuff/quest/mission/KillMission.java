package com.cdogsnappy.snappystuff.quest.mission;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class KillMission extends Mission {
    public EntityType toKill;
    public int numKills;
    public int numToKill;
    protected boolean complete = false;
    public KillMission(EntityType mob, int num){
        missionType = Type.KILL;
        toKill = mob;
        numToKill = num;
        numKills = 0;
    }
    public KillMission(EntityType mob, int numToKill, int numKills, boolean complete){
        missionType = Type.KILL;
        this.toKill = mob;
        this.numToKill = numToKill;
        this.numKills = numKills;
        this.complete = complete;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }
    @Override
    public boolean attemptComplete(){
        if(numKills >= numToKill){
            return complete = true;
        }
        return false;
    }

    public CompoundTag save(CompoundTag tag){
        tag.putInt("type",0);
        tag.putString("entity", ForgeRegistries.ENTITY_TYPES.getKey(toKill).toString());
        tag.putInt("numKills",numKills);
        tag.putInt("numToKill",numToKill);
        tag.putBoolean("complete",complete);
        return tag;
    }
    public static KillMission load(CompoundTag tag){
        EntityType e = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(tag.getString("entity")));
        int numKills = tag.getInt("numKills");
        int numToKill = tag.getInt("numToKill");
        boolean complete = tag.getBoolean("complete");
        return new KillMission(e,numToKill,numKills,complete);
    }
}