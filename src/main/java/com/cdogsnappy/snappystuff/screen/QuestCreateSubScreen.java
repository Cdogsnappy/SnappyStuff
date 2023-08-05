package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class QuestCreateSubScreen<C extends QuestCreateMenu, P extends QuestCreateScreen> extends QuestCreateScreen {

    private static final ResourceLocation QUEST_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_accept_block_gui.png");
    private final P parent;
    public QuestCreateSubScreen(P p) {
        super(p.getMenu(),p.getMenu().getPlayerInventory(),p.getTitle());
        parent = p;
    }

    class MissionSelectButton extends AbstractButton {
        public enum Type{
            BLOCK,
            COLLECT,
            KILL,
            PLAYERKILL
        }

        public MissionSelectButton(int p_93365_, int p_93366_, int p_93367_, int p_93368_, Component p_93369_) {
            super(p_93365_, p_93366_, p_93367_, p_93368_, p_93369_);
        }

        @Override
        public void onPress() {

        }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }
}
