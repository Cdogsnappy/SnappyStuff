package com.cdogsnappy.snappystuff.screen;


import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.network.UploadPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.RecordItem;

public class MusicUploadScreen extends AbstractContainerScreen<MusicUploadMenu> {
    private UploadButton button;
    private int progress = 0;
    private int completionProgress = 80;
    private boolean isProcessing = false;
        private static final ResourceLocation TEXTURE =
                new ResourceLocation(SnappyStuff.MODID,"textures/gui/music_upload_gui.png");

        public MusicUploadScreen(MusicUploadMenu menu, Inventory inventory, Component component) {
            super(menu, inventory, component);
        }

        @Override
        protected void init() {
            super.init();
            button = this.addRenderableWidget(new UploadButton(this.leftPos + 128,this.topPos + 64,38,12,Component.literal("UPLOAD")));
        }


    @Override
        protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

            renderProgressArrow(pPoseStack);
        }

        private void renderProgressArrow(PoseStack pPoseStack) {
            if(isProcessing) {
                blit(pPoseStack, this.leftPos + 104, this.topPos + 53-getScaledProgress(), 176, 28 - getScaledProgress(), 12, getScaledProgress());
            }
        }

        @Override
        public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
            renderBackground(pPoseStack);
            super.render(pPoseStack, mouseX, mouseY, delta);
            renderTooltip(pPoseStack, mouseX, mouseY);
        }

        public class UploadButton extends AbstractButton {

            public UploadButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
                super(pX, pY, pWidth, pHeight, pMessage);
            }

            @Override
            public void onPress() {
                if(menu.slots.get(36).getItem().getItem() instanceof RecordItem){isProcessing = true;}
                else{minecraft.player.sendSystemMessage(Component.literal(menu.slots.get(36).getItem().toString()));}
            }

            @Override
            public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

            }
        }
        @Override
    public void containerTick(){
            if(isProcessing){
                ++progress;
                if(progress >= completionProgress){
                    SnappyNetwork.sendToServer(new UploadPacket((menu.getSlot(36).getItem())));
                    isProcessing = false;
                    progress = 0;
                }
            }
        }
    public int getScaledProgress() {// Max Progress
        int progressArrowSize = 28; // This is the height in pixels of your arrow

        return completionProgress != 0 && progress != 0 ? progress * progressArrowSize / completionProgress : 0;
    }
}

