package com.cdogsnappy.snappystuff.quest;

public class Mission implements IMission{
    @Override
    public boolean isComplete() {
        return false;
    }

    @Override
    public boolean attemptComplete() {return false;}

    public enum Type{
        KILL,
        COLLECT
    }
    Type missionType;
}
