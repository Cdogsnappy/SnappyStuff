package com.cdogsnappy.snappymod.karma;

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
    public UUID getID(){
        return id;
    }
    public LocalDateTime getTime(){
        return time;
    }
}
