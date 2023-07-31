package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class MissionHandler {

    /**
     * Store in player instanced lists so that we don't have to iterate through every mission every time we make a check.
     */
    public static HashMap<UUID,List<BlockMission>> blockMissionList = new HashMap<>();
    public static HashMap<UUID,List<PlayerKillMission>> playerKillMissionList = new HashMap<>();

    /**
     * Checks if a murder fulfills a contract
     * @param murdered
     * @param murderer
     */
    public static void onPlayerMurder(Player murdered, Player murderer){
        if(playerKillMissionList.containsKey(murderer.getUUID())){
            List<PlayerKillMission> missions = playerKillMissionList.get(murderer.getUUID());
            for(PlayerKillMission mission : missions){
                if(mission.hitman == murderer.getUUID() && mission.toKill == murdered.getUUID()){
                    mission.completeMission();
                    missions.remove(mission);
                    if(missions.isEmpty()){
                        playerKillMissionList.remove(murderer.getUUID());
                    }
                    else {
                        playerKillMissionList.put(murderer.getUUID(), missions);
                    }
                    return;
                }
            }
        }

    }

    /**
     * @author Cdogsnappy
     * Handles BlockMissions, checks mission progress every time a player breaks a block
     * @param event
     */
    @SubscribeEvent
    public static void onBreak(PlayerEvent.BreakSpeed event){
        if(event.getEntity().level.isClientSide){
            return;
        }
        Player player = event.getEntity();
        if(blockMissionList.containsKey(player.getUUID())){
            List<BlockMission> missions = blockMissionList.get(player.getUUID());
            for(BlockMission mission : missions){
                if(player.getUUID() == mission.player && event.getState().getBlock() == mission.toBreak){
                    mission.numBroken++;
                    if(mission.attemptComplete()){
                        missions.remove(mission);
                        if(missions.isEmpty()){
                            playerKillMissionList.remove(player.getUUID());
                        }
                        else {
                            blockMissionList.put(player.getUUID(),missions);
                        }
                    }
                    return;
                }
            }
        }
    }


}
