package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class MissionHandler {

    /**
     * Store in player instanced lists so that we don't have to iterate through every mission every time we make a check.
     * THESE LISTS ONLY STORE INCOMPLETE MISSIONS, ONCE A MISSION IS COMPLETE REMOVE IT, IT WILL BE TRACKED BY IT'S QUEST OBJECT
     */
    public static HashMap<UUID,List<BlockMission>> blockMissionList = new HashMap<>();
    public static HashMap<UUID,List<PlayerKillMission>> playerKillMissionList = new HashMap<>();
    public static HashMap<UUID,List<KillMission>> killMissionList = new HashMap<>();
    public static List<DailyMission> dailyMissionList = new ArrayList<>();

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
     * checks if a player killed a mob that they need to kill
     * @param killed mob that was killed
     * @param killer player that killed
     */
    public static void onEntityKill(LivingEntity killed, Player killer){
        if(!killMissionList.containsKey(killer.getUUID())){
            return;
        }
        List<KillMission> killMissions = killMissionList.get(killer.getUUID());
        for(KillMission k : killMissions){
            if(k.toKill == killed.getType()){
                k.numKills++;
                if(k.attemptComplete()){
                    killMissions.remove(k);
                }
            }
        }
        if(killMissions.isEmpty()){
            killMissionList.remove(killer.getUUID());
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

    /**
     * @author Cdogsnappy
     * Run on server start, loads all ACTIVE missions into the mission handler maps.
     * @param missions list of accepted missions for a quest
     * @param player the player who is currently doing the quest
     */
    public static void loadMissions(List<Mission> missions, UUID player){
        missions.forEach((m) -> {
            if(!m.isComplete()){//DO NOT ADD COMPLETED MISSIONS TO THE MAPS
                switch(m.missionType){
                    case KILL:
                        List<KillMission> killMissions = killMissionList.get(player);
                        if(killMissions == null){killMissions = new ArrayList<>();}
                        killMissions.add((KillMission)m);
                        killMissionList.put(player, killMissions);
                    case KILL_PLAYER:
                        List<PlayerKillMission> playerKillMissions = playerKillMissionList.get(player);
                        if(playerKillMissions == null){playerKillMissions = new ArrayList<>();}
                        playerKillMissions.add((PlayerKillMission)m);
                        playerKillMissionList.put(player, playerKillMissions);
                    case BLOCK:
                        List<BlockMission> blockMissions = blockMissionList.get(player);
                        if(blockMissions == null){blockMissions = new ArrayList<>();}
                        blockMissions.add((BlockMission)m);
                        blockMissionList.put(player, blockMissions);
                    default:
                        break;
                }

            }

        });
    }


}
