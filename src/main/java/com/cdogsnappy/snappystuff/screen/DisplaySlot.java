package com.cdogsnappy.snappystuff.screen;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DisplaySlot extends SlotItemHandler {
    public DisplaySlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack p_40231_){return false;}

    @Override
    public boolean mayPickup(Player p_40228_){return false;}
}
