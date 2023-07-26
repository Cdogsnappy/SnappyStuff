package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.karma.EndorsementHandler;
import com.cdogsnappy.snappystuff.karma.SmiteHandler;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TickingHandler {

    /**
     * Handles every snappystuff action that requires being called every server tick
     * @param event The ServerTickEvent
     */
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        RadioHandler.onTick();
        SmiteHandler.onTick(event);
    }
}
