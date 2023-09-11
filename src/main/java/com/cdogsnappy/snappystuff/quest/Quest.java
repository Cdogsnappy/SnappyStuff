package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.UUID;

public class Quest {
    public enum QuestType{
        PLAYER,
        DAILY;


    }
    @NonNull
    protected QuestType type;
    public List<ItemStack> rewards;
    @NonNull
    public UUID questID;//UNIQUE IDENTIFIER
    @NonNull
    public UUID requestor;//Player who created quest(NON-NULLABLE)
    public static final UUID radiantID = UUID.fromString("558d5358-75c9-4a7a-a5e7-e4ccbd7ccf4c");//USE THIS FOR NON PLAYER CREATED QUESTS
}
