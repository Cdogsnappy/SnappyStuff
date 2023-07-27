package com.cdogsnappy.snappystuff.screen;


import com.cdogsnappy.snappystuff.SnappyStuff;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MusicUploadScreen extends AbstractContainerScreen<MusicUploadMenu> {
        private static final ResourceLocation TEXTURE =
                new ResourceLocation(SnappyStuff.MODID,"textures/gui/music_upload_gui.png");

        public MusicUploadScreen(MusicUploadMenu menu, Inventory inventory, Component component) {
            super(menu, inventory, component);
        }

        @Override
        protected void init() {
            super.init();
        }


    @Override
        protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

            renderProgressArrow(pPoseStack, x, y);
        }

        private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
            if(menu.isUploading()) {
                blit(pPoseStack, x + 104, y + 53, 176, 28, 12, -menu.getScaledProgress());
            }
        }

        @Override
        public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
            renderBackground(pPoseStack);
            super.render(pPoseStack, mouseX, mouseY, delta);
            renderTooltip(pPoseStack, mouseX, mouseY);
        }
    }

