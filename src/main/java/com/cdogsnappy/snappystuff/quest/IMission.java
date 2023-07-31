package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;

public interface IMission {

    boolean isComplete();

    boolean attemptComplete();
    CompoundTag save(CompoundTag tag);
}
