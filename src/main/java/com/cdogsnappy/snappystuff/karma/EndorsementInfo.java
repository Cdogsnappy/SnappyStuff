package com.cdogsnappy.snappystuff.karma;

import net.minecraft.nbt.CompoundTag;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class EndorsementInfo implements Serializable {
    protected UUID id;
    protected LocalDateTime time;
    public EndorsementInfo(UUID id){
        this.id = id;
        this.time = LocalDateTime.now().plusDays(1);
    }
    public EndorsementInfo(UUID id, LocalDateTime time){
        this.id = id;
        this.time = time;
    }
    public UUID getID(){
        return id;
    }
    public LocalDateTime getTime(){
        return time;
    }

    public String toString(){
        return id + ", " + time;
    }

    public CompoundTag save(CompoundTag tag){
        tag.putUUID("id",id);
        tag.putString("time",time.toString());
        return tag;
    }
    public static EndorsementInfo load(CompoundTag tag){
        return new EndorsementInfo(tag.getUUID("id"), LocalDateTime.parse(tag.getString("time")));
    }
}
