package com.cdogsnappy.snappystuff.quest;

import java.io.Serializable;

public class Mission implements IMission, Serializable {
    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean attemptComplete() {return false;}

    public enum Type{
        KILL,
        COLLECT,
        BLOCK
    }
    Type missionType;
}
