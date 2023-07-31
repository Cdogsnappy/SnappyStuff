package com.cdogsnappy.snappystuff.quest;

import java.util.UUID;

public class PlayerKillMission extends Mission{
    protected UUID toKill;
    protected UUID hitman;
    protected boolean complete = false;
    public PlayerKillMission(UUID toKill, UUID hitman) {
        this.toKill = toKill;
        this.hitman = hitman;
    }
    public boolean completeMission(){return complete = true;}
    public boolean isComplete(){return complete;}
}
