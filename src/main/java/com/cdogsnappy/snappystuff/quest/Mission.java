package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import java.io.Serializable;

public class Mission extends SavedData implements IMission, Serializable {
    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean attemptComplete() {return false;}

    @Override
    public CompoundTag save(CompoundTag p_77763_) {
        return null;
    }


    public static IMission load(CompoundTag tag) {
        if(!tag.contains("type")){
            return null;
        }
        int type = tag.getInt("type");
        switch(type){
            case 0:
                return KillMission.load(tag);
            case 1:
                return BlockMission.load(tag);
            case 2:
                return CollectMission.load(tag);
            case 3:
                return PlayerKillMission.load(tag);
            default:
                return null;
        }
    }

    public enum Type{
        KILL,
        COLLECT,
        BLOCK
    }
    Type missionType;
}
