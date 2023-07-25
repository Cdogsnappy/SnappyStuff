package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.blocks.ModBlocks;
import com.cdogsnappy.snappystuff.blocks.ModEntityBlocks;
import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.screen.ModMenus;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class Registration {


    public static void register(IEventBus bus){
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        SSSoundRegistry.SOUNDS.register(bus);
        ModEntityBlocks.BLOCK_ENTITIES.register(bus);
        ModMenus.MENUS.register(bus);

    }
}
