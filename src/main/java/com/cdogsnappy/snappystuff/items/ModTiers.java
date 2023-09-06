package com.cdogsnappy.snappystuff.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier DIVINIUM = new ForgeTier(5,4000,10.0f,-2.0f,25, BlockTags.NEEDS_DIAMOND_TOOL,() -> {return Ingredient.of(ModItems.DIVINIUM_INGOT.get());});
    public static final ForgeTier DEMON = new ForgeTier(5,4000,10.0f,7.0f,25, BlockTags.NEEDS_DIAMOND_TOOL,() -> {return Ingredient.of(ModItems.DEMONIC_INGOT.get());});
    public static final ForgeTier NEUTRALIUM = new ForgeTier(5,4000,10.0f,5.0f,25, BlockTags.NEEDS_DIAMOND_TOOL,() -> {return Ingredient.of(ModItems.NEUTRALIUM_INGOT.get());});

}
