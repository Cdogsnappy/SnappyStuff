package com.cdogsnappy.snappystuff.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class NumberBox extends EditBox {
    public NumberBox(Font p_94114_, int p_94115_, int p_94116_, int p_94117_, int p_94118_, Component p_94119_) {
        super(p_94114_, p_94115_, p_94116_, p_94117_, p_94118_, p_94119_);
    }


    /**
     * Only allow numbers to be typed
     * @param c character
     * @return whether successful
     */
    @Override
    public boolean charTyped(char c, int p_94123_) {
        if(Character.isDigit(c)){
            return super.charTyped(c,p_94123_);
        }
        return false;
    }
}
