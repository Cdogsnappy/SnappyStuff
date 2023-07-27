package com.cdogsnappy.snappystuff.radio;

import java.io.Serializable;

public class SoundInfo implements Serializable {
    protected int index;
    protected int lengthInTicks;
    public SoundInfo(int index, int lengthInTicks){
        this.index = index;
        this.lengthInTicks = lengthInTicks;
    }
}
