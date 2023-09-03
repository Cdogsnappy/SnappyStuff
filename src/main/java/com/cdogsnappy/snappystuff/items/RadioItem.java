package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.network.SoundStopPacketS2C;
import com.cdogsnappy.snappystuff.network.StandbyResetPacket;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
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
        RadioHandler.standbyListeners.add((Player) slotContext.getWearer());

    }

    /**
     * @author Cdogsnappy
     * @param slotContext Context about the slot that the ItemStack was just unequipped from
     * @param prevStack    The new ItemStack in the slot
     * @param stack       The ItemStack in question
     */
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player p = (Player) slotContext.getWearer();
        RadioHandler.standbyListeners.remove(p);
        RadioHandler.listeners.remove(p);
        SnappyNetwork.sendToPlayer(new StandbyResetPacket(),(ServerPlayer) p);
        SnappyNetwork.sendToNearbyPlayers(new SoundStopPacketS2C(p.getUUID()),p.position(),p.level.dimension());
    }
}
