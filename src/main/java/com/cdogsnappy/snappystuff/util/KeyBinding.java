package com.cdogsnappy.snappystuff.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_SNAPPYSTUFF = "key.category.snappystuff.quest";
    public static final String KEY_OPEN_QUESTBOOK = "key.snappystuff.open_questbook";
    public static final KeyMapping QUEST_KEY = new KeyMapping(KEY_OPEN_QUESTBOOK, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY_SNAPPYSTUFF);
}
