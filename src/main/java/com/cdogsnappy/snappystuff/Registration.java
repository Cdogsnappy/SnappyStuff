package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.blocks.ModBlocks;
import com.cdogsnappy.snappystuff.blocks.ModEntityBlocks;
import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.screen.ModMenus;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class Registration {


    public static void register(IEventBus bus){
        bus.register(ModBlocks.BLOCKS);
        ModItems.ITEMS.register(bus);
        bus.register(SSSoundRegistry.SOUNDS);
        bus.register(ModEntityBlocks.BLOCK_ENTITIES);
        bus.register(ModMenus.MENUS);

    }
}
