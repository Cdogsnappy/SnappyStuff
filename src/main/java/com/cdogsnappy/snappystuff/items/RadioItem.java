package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.radio.RadioHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class RadioItem extends Item implements ICurioItem {

    public RadioItem(Properties p_41383_) {
        super(p_41383_);
    }

    /**
     * @author Cdogsnappy
     * @param slotContext Context about the slot that the ItemStack was just unequipped from
     * @param prevStack   The previous ItemStack in the slot
     * @param stack       The ItemStack in question
     */
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        RadioHandler.listeners.add((Player) slotContext.getWearer());

    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        RadioHandler.listeners.remove((Player) slotContext.getWearer());
        RadioHandler.playingSounds.remove((Player) slotContext.getWearer());
        Minecraft.getInstance().getSoundManager().stop(RadioHandler.playingSounds.remove((Player) slotContext.getWearer()));
    }
}
