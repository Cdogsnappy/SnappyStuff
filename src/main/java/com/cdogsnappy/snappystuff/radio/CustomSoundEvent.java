package com.cdogsnappy.snappystuff.radio;

import net.minecraft.sounds.SoundEvent;

public class CustomSoundEvent {
    protected SoundEvent sound;
    protected int tickLength;
    public CustomSoundEvent(){
        sound = null;
        tickLength = 0;
    }
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
