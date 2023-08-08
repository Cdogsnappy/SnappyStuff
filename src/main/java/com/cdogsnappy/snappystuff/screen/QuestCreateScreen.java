package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;

import net.minecraft.world.item.ItemStack;

import com.cdogsnappy.snappystuff.screen.QuestScreensData;


public class QuestCreateScreen extends AbstractContainerScreen<QuestCreateMenu> {
    protected List<Mission> missions;
    private ResourceLocation MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private List<AbstractButton> buttons = Lists.newArrayList();
    private int page = 0;
    private int numPages = 0;
    private static final int startSearchTilesX = 30;
    private static final int startSearchTilesY = 32;
    private static final int HIDDEN_POS = 9999;
    private Object selectedObject;
    private int counter = 1;
    private boolean isSearching = false;
    private List<ObjectSelectButton> searchButtons = Lists.newArrayList();
    private enum QuestScreensData.ButtonType currentSearchMenu = QuestScreensData.ButtonType.COLLECT;

    public QuestCreateScreen(QuestCreateMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 256;
        this.imageHeight = 234;
        this.titleLabelX = 12;
        this.titleLabelY = 8;
        this.inventoryLabelX = 22;
        this.inventoryLabelY = this.imageHeight - 104;
    }

    @Override
    protected void init() {
        super.init();
        buttons.add(new QuestCreateMissionButton(240,26,27,13,Component.literal("EDIT"),this));
        buttons.add(new QuestCreateMissionButton(240,66,27,13,Component.literal("EDIT"),this));
        buttons.add(new QuestCreateMissionButton(240,105,27,13, Component.literal("EDIT"),this));
        this.buttons.forEach((b) ->{
            this.addRenderableWidget(b);
        });
        for(int j = 0; j<40; ++j){
            searchButtons.add(new ObjectSelectButton(HIDDEN_POS,HIDDEN_POS,18,18, null));
        }
        searchButtons.forEach((b) ->{
            this.addRenderableWidget(b);
        });
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, MAIN_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

    }
    private void renderButtonSymbols(){
        searchButtons.forEach((b) ->{
            b.x = HIDDEN_POS;
            b.y = HIDDEN_POS;
        });
        int numButtons = 0;
        if(page == numPages){
            numButtons = QuestScreensData.filteredTokens.size() % 40;
        }
        else{
            numButtons = 40;
        }
        for(int j = 0; j < 5; ++j){
            for(int i = 0; i < 8; ++i){
                if((j*5)+i > numButtons){
                    return;
                }
                ObjectSelectButton b = searchButtons.get((j*5)+i);
                b.x = 30 + 18*i;
                b.y = 32 + 18*j;
                switch(this.currentSearchMenu){
                    case COLLECT:
                        drawItem((30+18*i) + 1, (32 + 18*j) + 1, (ItemStack)QuestScreensData.filteredTokens.get((j*5)+i));

                }
            }
        }
    }

    public class QuestCreateMissionButton extends AbstractButton {

        private boolean selected;
        private QuestCreateScreen qcs;
        public QuestCreateMissionButton(int x, int y, int w, int l, Component text, QuestCreateScreen qcs) {
            super(x,y,w,l,text);
            this.qcs = qcs;
        }
        @Override
        public void onPress() {
            this.qcs.buttons.forEach((b) ->{
                b.x = HIDDEN_POS;
                b.y = HIDDEN_POS;
            });
            this.qcs.MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/mission_create_gui.png");
        }

        public boolean isSelected() { return this.selected; }
        public void setSelected(boolean selected) { this.selected = selected; }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }
    public class ObjectSelectButton extends AbstractButton {

        private Object target;


        public ObjectSelectButton(int p_93365_, int p_93366_, int p_93367_, int p_93368_, Object obj) {
            super(p_93365_, p_93366_, p_93367_, p_93368_, Component.empty());
            this.target = obj;
        }

        @Override
        public void onPress() {

        }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
        public void renderButton(PoseStack p_93676_, int p_93677_, int p_93678_, float p_93679_) {
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            int i = this.getYImage(this.isHoveredOrFocused());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(p_93676_, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            this.blit(p_93676_, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.renderBg(p_93676_, minecraft, p_93677_, p_93678_);
            int j = getFGColor();
            drawCenteredString(p_93676_, font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }



    public void drawItem(int x, int y, ItemStack is) {
        this.itemRenderer.blitOffset = 100.0F;
        this.itemRenderer.renderAndDecorateItem(is, x, y);
        this.itemRenderer.blitOffset = 0.0F;
    }
}
