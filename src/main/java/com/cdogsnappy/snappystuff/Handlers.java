package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.items.DivineFruitItem;
import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.karma.KarmaLog;
import com.cdogsnappy.snappystuff.karma.SmiteHandler;
import com.cdogsnappy.snappystuff.network.AvailablePlayersPacket;
import com.cdogsnappy.snappystuff.network.QuestNetwork;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.quest.DailyQuestHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Handlers {
    static int ticker = 0;

    /**
     * Handles every snappystuff action that is run every server tick
     * @param event The ServerTickEvent
     */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        ticker++;
        if(ticker > 36000){
            SmiteHandler.judge(event.getServer());
            DailyQuestHandler.checkRefresh();
        }
        //RadioHandler.onTick();
    }

    /**
     * Handles every snappystuff action that is run on player login
     * @param event the PlayerLoggedInEvent
     */
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        if(event.getEntity().level.isClientSide){
            return;
        }
        CitizenData.onPlayerJoin(event.getEntity());
        Karma.playerCheck(event);
        KarmaLog.onPlayerJoin(event.getEntity());
        DivineFruitItem.addTag(event.getEntity());
        DivineFruitItem.updateDivineHealth(event.getEntity());
        QuestNetwork.sendToPlayer(new AvailablePlayersPacket(CitizenData.citizenNames),(ServerPlayer)event.getEntity());

    }

    /**
     * Handles every snappystuff action that is run on player respawn
     * @param event the PlayerRespawnEvent
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        if(event.getEntity().level.isClientSide){
            return;
        }
        DivineFruitItem.updateDivineHealth(event.getEntity());
        Karma.playerCheck(event);
    }
    /**
     * @author Cdogsnappy
     * Handles every snappystuff action that is run when a player kills something
     * @param event the death event of an entity
     */
    @SubscribeEvent
    public static void onPlayerKilled(LivingDeathEvent event){
        if(!event.getEntity().level.isClientSide && event.getEntity().getKillCredit() instanceof Player murderer){
            if(event.getEntity() instanceof Player){
                QuestHandler.onMurder((Player)event.getEntity(), murderer);
                MissionHandler.onPlayerMurder((Player)event.getEntity(),murderer);
                Karma.setScore(murderer,Karma.getScore(murderer.getUUID()) - Karma.adjustedKarmaValue(Karma.getScore(murderer.getUUID()),10));
            }
            else{
                MissionHandler.onEntityKill(event.getEntity(),murderer);
            }


        }
    }
}
