package com.cdogsnappy.snappystuff.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class SnappyStuffTabs {
    public static final CreativeModeTab SNAPPY_STUFF_TAB = new CreativeModeTab("snappystuff") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.RADIO.get());
        }
    };

    public static final CreativeModeTab SNAPPY_MUSIC_TAB = new CreativeModeTab("snappymusic") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.DREAM_SWEET_IN_SEA_MAJOR.get());
        }
    };
}
