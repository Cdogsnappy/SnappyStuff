package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.List;

public class KillMission extends Mission {
    public static List<EntityType<?>> entities = Arrays.asList();//Place possible mob targets in this list
    protected EntityType toKill;
    protected int numKills;
    protected int numToKill;
    protected boolean complete = false;
    public KillMission(EntityType mob, int num){
        missionType = Type.KILL;
        toKill = mob;
        numToKill = num;
        numKills = 0;
    }
    public KillMission(EntityType mob, int numToKill, int numKills, boolean complete){
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
        tag.putInt("entity",entities.indexOf(toKill));
        tag.putInt("numKills",numKills);
        tag.putInt("numToKill",numToKill);
        tag.putBoolean("complete",complete);
        return tag;
    }
    public static KillMission load(CompoundTag tag){
        EntityType target = entities.get(tag.getInt("entity"));
        int numKills = tag.getInt("numKills");
        int numToKill = tag.getInt("numToKill");
        boolean complete = tag.getBoolean("complete");

        return new KillMission(target,numToKill,numKills,complete);

    }
}
