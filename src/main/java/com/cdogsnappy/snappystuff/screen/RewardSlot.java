package com.cdogsnappy.snappystuff.screen;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RewardSlot extends SlotItemHandler {
    private boolean isActive = true;
    public RewardSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack p_40231_){return false;}
    @Override
    public boolean isActive(){return isActive;}
    public void setActive(boolean val){isActive = val;}
}
