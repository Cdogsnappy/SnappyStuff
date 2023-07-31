package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class QuestAcceptScreen extends AbstractContainerScreen<QuestAcceptMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_accept_block_gui.png");
    public QuestAcceptScreen(QuestAcceptMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }
    private Font font1;

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    //use PoseStack.scale -> PoseStack.scale(float x, float y, float z) to scale the text
    // Font.draw to render the text needed -> this.font.draw(PoseStack, float x, float y, int color)
    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
        Font font1 = this.font;
        pPoseStack.scale(.5f, .5f, .5f);
        this.font.draw(pPoseStack, "Test1", 50, 100, 0);
        this.font.draw(pPoseStack, "Test2", (float) 100, (float) 150, 1);
        this.font.draw(pPoseStack, "Test3", 150, 100, 255);
    }
}
