package com.cdogsnappy.snappystuff.quest.mission;

import net.minecraft.nbt.CompoundTag;

public interface IMission {

    boolean isComplete();

    boolean attemptComplete();
    CompoundTag save(CompoundTag tag);

}