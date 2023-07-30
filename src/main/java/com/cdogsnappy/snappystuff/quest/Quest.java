package com.cdogsnappy.snappystuff.quest;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Quest {
    protected List<IMission> missions;
    protected List<ItemStack> reward;

    public Quest(List<IMission> missions, List<ItemStack> rewards){
        this.missions = missions;
        this.reward = rewards;

    }

}
