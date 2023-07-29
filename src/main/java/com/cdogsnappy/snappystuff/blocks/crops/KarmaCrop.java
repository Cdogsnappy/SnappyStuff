package com.cdogsnappy.snappystuff.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;


public class KarmaCrop extends NonRegenCropBlock{

    public KarmaCrop(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter p_52258_, BlockPos p_52259_, BlockState p_52260_, boolean p_52261_) {
        return false;
    }

    /**
     * Check if the block is going to be broken, if so then augment the players karma
     * @param event LeftClickBlock event
     */
    @SubscribeEvent
    public static void onHarvest(PlayerInteractEvent.LeftClickBlock event){
        if(event.getUseBlock() == Event.Result.ALLOW){
            UUID breaker = event.getEntity().getUUID();

        }
    }
}
