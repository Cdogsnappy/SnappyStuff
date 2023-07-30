package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MissionHandler {
    public static List<BlockMission> blockMissionList = new ArrayList<BlockMission>();
    public static List<PlayerKillMission> playerKillMissionList = new ArrayList<>();

    public static void onPlayerMurder(Player murdered, Player murderer){
        for(PlayerKillMission mission : playerKillMissionList){
            if(mission.hitman == murderer.getUUID() && mission.toKill == murdered.getUUID()){
                mission.completeMission();
                return;
            }
        }
    }

}
