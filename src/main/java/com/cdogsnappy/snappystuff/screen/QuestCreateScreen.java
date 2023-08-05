package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class QuestCreateScreen extends AbstractContainerScreen<QuestCreateMenu> {
    private static final ResourceLocation MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private static final ResourceLocation QUEST_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_accept_block_gui.png");

    public QuestCreateScreen(QuestCreateMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }


    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {


    }

    abstract static class QuestCreateScreenButton extends AbstractButton {

        private boolean selected;
        public QuestCreateScreenButton(int x, int y, int w, int l, Component text) {
            super(x,y,w,l,text);
        }

        public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/quest_create_block_gui"));
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 219;
            int j = 0;
            if (!this.active) {
                j += this.width * 2;
            } else if (this.selected) {
                j += this.width * 1;
            } else if (this.isHoveredOrFocused()) {
                j += this.width * 3;
            }

            this.blit(pPoseStack, this.x, this.y, j, 219, this.width, this.height);
            this.renderIcon(pPoseStack);
        }
        public boolean isSelected() { return this.selected; }
        public void setSelected(boolean selected) { this.selected = selected; }
        protected abstract void renderIcon(PoseStack pPoseStack);
    }
}
