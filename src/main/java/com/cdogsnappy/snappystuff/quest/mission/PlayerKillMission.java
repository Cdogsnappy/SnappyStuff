package com.cdogsnappy.snappystuff.quest.mission;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class PlayerKillMission extends Mission {
    protected UUID toKill;
    protected UUID hitman;
    protected boolean complete = false;
    public PlayerKillMission(UUID toKill) {
        this.missionType = Type.KILL_PLAYER;
        this.toKill = toKill;
    }
    public PlayerKillMission(UUID toKill, UUID hitman, boolean complete){
        this.missionType = Type.KILL_PLAYER;
        this.toKill = toKill;
        this.hitman = hitman;
    }
    public boolean completeMission(){return complete = true;}
    public boolean isComplete(){return complete;}
    public UUID getTarget(){return toKill;}

    @Override
    public CompoundTag save(CompoundTag tag){
        tag.putInt("type",3);
        tag.putUUID("toKill",toKill);
        tag.putUUID("hitman",hitman);
        tag.putBoolean("complete",complete);
        return tag;
    }
    public static PlayerKillMission load(CompoundTag tag){
        UUID toKill = tag.getUUID("toKill");
        UUID hitman = tag.getUUID("hitman");
        boolean complete = tag.getBoolean("complete");
        return new PlayerKillMission(toKill,hitman,complete);
    }
}