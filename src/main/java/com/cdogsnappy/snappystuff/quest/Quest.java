package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class Quest {
    protected List<ItemStack> rewards;
    protected UUID requestor;//Player who created quest(NULLABLE)
    protected static final UUID radiantID = UUID.fromString("558d5358-75c9-4a7a-a5e7-e4ccbd7ccf4c");//USE THIS FOR NON PLAYER CREATED QUESTS
}
