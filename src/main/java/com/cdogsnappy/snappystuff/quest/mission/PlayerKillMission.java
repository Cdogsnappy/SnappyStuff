package com.cdogsnappy.snappystuff.quest.mission;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class PlayerKillMission extends Mission {
    public UUID toKill;
    public String player;
    protected boolean complete = false;
    public PlayerKillMission(UUID toKill, String player) {
        this.missionType = Type.KILL_PLAYER;
        this.toKill = toKill;
        this.player = player;
    }
    public PlayerKillMission(UUID toKill, String name, boolean complete){
        this.missionType = Type.KILL_PLAYER;
        this.toKill = toKill;
        this.player = name;
        this.complete = complete;
    }
    public boolean isComplete(){return complete;}
    public boolean attemptComplete(){return complete;}//Nothing to attempt here, these missions automatically complete.
    public UUID getTarget(){return toKill;}
    @Override
    public CompoundTag save(CompoundTag tag){
        tag.putInt("type",3);
        tag.putUUID("toKill",toKill);
        tag.putBoolean("complete",complete);
        tag.putString("name",player);
        return tag;
    }
    public static PlayerKillMission load(CompoundTag tag){
        UUID toKill = tag.getUUID("toKill");
        UUID hitman = null;
        boolean complete = tag.getBoolean("complete");
        String player = tag.getString("name");
        return new PlayerKillMission(toKill, player, complete);
    }
}