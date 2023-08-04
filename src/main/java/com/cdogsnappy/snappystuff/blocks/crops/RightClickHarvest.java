package com.cdogsnappy.snappystuff.blocks.crops;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class RightClickHarvest {
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event){
        if(onRightClick(event.getLevel(), event.getEntity(),event.getHand(),event.getHitVec()) != InteractionResult.PASS){
            event.setCanceled(true);

        }
    }
    public static InteractionResult onRightClick(Level level, Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hitResult){
        BlockState state = level.getBlockState(hitResult.getBlockPos());
        if(level.isClientSide || !(state.getBlock() instanceof CropBlock)){
            return InteractionResult.PASS;
        }

        CropBlock crop = (CropBlock)state.getBlock();
        if(crop.isMaxAge(state)){
            List<ItemStack> items = Block.getDrops(state,(ServerLevel)level, hitResult.getBlockPos(),null, player, player.getItemInHand(hand));
            SimpleContainer inv = new SimpleContainer(items.size());
            for(ItemStack i : items){
                inv.addItem(i);
            }
            Containers.dropContents(level, hitResult.getBlockPos(), inv);
            if(state.getBlock() instanceof NonRegenCropBlock){
                level.removeBlock(hitResult.getBlockPos(),true);
            }
            else{
                level.setBlockAndUpdate(hitResult.getBlockPos(),crop.getStateForAge(0));
            }

        }
        return InteractionResult.PASS;
    }
}
