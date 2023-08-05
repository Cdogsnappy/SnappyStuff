package com.cdogsnappy.snappystuff.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class QuestCreateSubMenu extends QuestCreateMenu{
    public QuestCreateSubMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(id, inv, extraData);
    }
}
