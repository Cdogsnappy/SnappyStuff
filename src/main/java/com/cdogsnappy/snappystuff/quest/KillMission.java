package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.entity.LivingEntity;

public class KillMission extends Mission {
    protected LivingEntity toKill;
    protected int numKills;
    protected int numToKill;
    protected boolean complete = false;
    public KillMission(LivingEntity entity, int num){
        missionType = Type.KILL;
        toKill = entity;
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
}
