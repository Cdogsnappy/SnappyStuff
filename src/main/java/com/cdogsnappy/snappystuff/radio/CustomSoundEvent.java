package com.cdogsnappy.snappystuff.radio;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.io.Serializable;

public class CustomSoundEvent {
    protected int tickLength;
    protected SoundEvent sound;
    public CustomSoundEvent(SoundEvent sound, int tickLength){
        this.sound = sound;
        this.tickLength = tickLength;
    }
    public SoundEvent getSound(){
        return this.sound;
    }
    public int getTickLength() {
        return this.tickLength;
    }
}
