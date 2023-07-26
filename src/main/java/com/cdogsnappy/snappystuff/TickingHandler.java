package com.cdogsnappy.snappymod;

import com.cdogsnappy.snappymod.karma.EndorsementHandler;
import com.cdogsnappy.snappymod.karma.SmiteHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TickingHandler {
    SmiteHandler smiteHandler = new SmiteHandler();
    int ticks = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        ticks++;
        if (ticks >= 400) {
            smiteHandler.judge(event.getServer());
            EndorsementHandler.updateCooldowns(event.getServer());
            ticks = 0;
        }
    }
}
