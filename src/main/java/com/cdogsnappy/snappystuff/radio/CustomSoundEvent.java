package com.cdogsnappy.snappystuff.radio;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.io.Serializable;

public class CustomSoundEvent extends SoundEvent implements Serializable {
    protected int tickLength;
    public CustomSoundEvent(ResourceLocation rL, int tickLength){
        super(rL);
        this.tickLength = tickLength;
    }
    public SoundEvent getSound(){
        return this;
    }
    public int getTickLength() {
        return this.tickLength;
    }
}
