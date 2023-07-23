package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.Radio.RadioHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class RadioItem extends Item implements ICurioItem {

    public RadioItem(Properties p_41383_) {
        super(p_41383_);
    }
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
