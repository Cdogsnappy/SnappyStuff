package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PlayerKillMission extends KillMission{
    public PlayerKillMission(Player entity) {
        super(entity, 1);
    }
}
