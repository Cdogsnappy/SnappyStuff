package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;

import javax.swing.text.html.parser.Entity;

public class KillMission extends Mission {
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

    public static IMission load(CompoundTag tag){
        return null;
    }
}
