package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.items.DivineFruitItem;
import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.karma.SmiteHandler;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Handlers {

    /**
     * Handles every snappystuff action that is run every server tick
     * @param event The ServerTickEvent
     */
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        //RadioHandler.onTick();
        SmiteHandler.onTick(event);
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
        DivineFruitItem.addTag(event.getEntity());
        DivineFruitItem.updateDivineHealth(event.getEntity());

    }

    /**
     * Handles ever snappystuff action that is run on player respawn
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
}
