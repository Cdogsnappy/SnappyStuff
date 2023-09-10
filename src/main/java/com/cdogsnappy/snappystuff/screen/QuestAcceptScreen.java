package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.network.QuestAcceptPacket;
import com.cdogsnappy.snappystuff.network.QuestRequestPacket;
import com.cdogsnappy.snappystuff.network.QuestScreenPacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.OpenContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestAcceptScreen extends QuestScreen<QuestAcceptMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private final int verticalPadding = 7;
    private final int horizontalPadding = 9;
    private Font font1;
    private PageButton prevButton;
    private PageButton nextButton;
    private final boolean playTurnSound = true;
    private int currentPage = 0;
    private Tab[] TABS = new Tab[2];

    private final List<QuestAcceptScreen.QuestButtons> buttonList = Lists.newArrayList();
    public QuestAcceptScreen(QuestAcceptMenu menu, Inventory inventory, Component component) {
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
        createPageControlButtons();
        this.addButton(new QuestAcceptScreen.QuestAcceptConfirmButton(this.leftPos + 195, this.topPos + 198));
        SnappyNetwork.sendToServer(new QuestRequestPacket(0,false));
        TABS[0] = this.addWidget(new Tab(this.leftPos + this.imageWidth-7,this.topPos + 30,null,0, new ItemStack(Items.IRON_PICKAXE), this));
        TABS[1] = this.addWidget(new Tab(this.leftPos + this.imageWidth-11,this.topPos+60,null,1, new ItemStack(ModItems.DEMON_BLADE.get()), this));
    }

    private <T extends AbstractWidget & QuestAcceptScreen.QuestButtons> void addButton(T button) {
        this.addRenderableWidget(button);
        this.buttonList.add(button);
    }

    interface QuestButtons {
        boolean isShowingTooltip();
        void renderToolTip(PoseStack pPoseStack, int x, int y);

        void updateStatus(int status);
    }

    abstract static class QuestAcceptScreenButton extends AbstractButton implements QuestAcceptScreen.QuestButtons {
        private boolean selected;
        protected QuestAcceptScreenButton(int x, int y) {
            super(x, y, 22, 22, CommonComponents.EMPTY); //22,22 is width,height
        }
        protected QuestAcceptScreenButton(int x, int y, Component component) {
            super(x, y, 44, 22, component);
        }
        public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, new ResourceLocation(SnappyStuff.MODID,"textures/gui/button_texture.png"));
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
            drawCenteredString(pPoseStack, Minecraft.getInstance().font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | colorTranslate(Color.WHITE));
        }

        protected abstract void renderIcon(PoseStack pPoseStack);
        public boolean isSelected() { return this.selected; }
        public void setSelected(boolean selected) { this.selected = selected; }
        public boolean isShowingTooltip() { return this.isHovered; }
        public void updateNarration(NarrationElementOutput buttonOutput) { this.defaultButtonNarrationText(buttonOutput); }
    }
    abstract class QuestAcceptSpriteScreenButton extends QuestAcceptScreenButton {
        private final int iconX;
        private final int iconY;

        QuestAcceptSpriteScreenButton(int x, int y, int iconX, int iconY, Component component) {
            super(x, y, component);
            this.iconX = iconX;
            this.iconY = iconY;
        }
        protected void renderIcon(PoseStack pPoseStack) {
            this.blit(pPoseStack, this.x + 2, this.y + 2, this.iconX, this.iconY, 18, 18);
        }
        public void renderToolTip (PoseStack pPoseStack, int mouseX, int mouseY) {
            QuestAcceptScreen.this.renderTooltip(pPoseStack, QuestAcceptScreen.this.title, mouseX, mouseY);
        }
    }

    class QuestAcceptConfirmButton extends QuestAcceptScreen.QuestAcceptSpriteScreenButton {
        public QuestAcceptConfirmButton(int x, int y) {
            super(x, y, 90, 220, Component.literal("ACCEPT"));
        }
        public void onPress() {
            if(QuestScreensData.questScreenDisplay == null){return;}
            SnappyNetwork.sendToServer(new QuestAcceptPacket((ClosedContractQuest)QuestScreensData.questScreenDisplay));
            if(currentPage >= getNumPages()-1){--currentPage;}//Weird code here, but we just removed a quest from the list of quests, so there is now length-1-1 quests.
            else{++currentPage;}
            //Send packets
        }
        public void updateStatus(int status) {
            //Remove quest from thinger
        }
    }
    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        TABS[0].render(pPoseStack,pMouseX,pMouseY,pPartialTick);
        TABS[1].render(pPoseStack,pMouseX,pMouseY,pPartialTick);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        if ((QuestScreensData.numOpenQuests == 0 && currTab == 1) || (QuestScreensData.numClosedQuests == 0 && currTab == 0)) {
            this.font.draw(pPoseStack, "No Quests!", x + 80 - (float) (this.font.width("No Quests!") / 2), y + 40, 0);
        }
        else {
            this.font.draw(pPoseStack, "Quest # " + (currentPage+1), this.leftPos + 192, this.topPos + 132, 0);
            switch(currTab){
                case 0:
                    this.font.draw(pPoseStack, "From " + CitizenData.getPlayerName(QuestScreensData.questScreenDisplay.requestor), this.leftPos + 192, this.topPos + 140, 0);
                    for(int j = 0; j < 3; ++j){renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43+(40*j),110,21,((ClosedContractQuest)QuestScreensData.questScreenDisplay).missions.get(j));}
                    break;
                case 1:
                    renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43,110,21,((OpenContractQuest)QuestScreensData.questScreenDisplay).mission);
                    break;
            }
        }
    }


    //use PoseStack.scale -> PoseStack.scale(float x, float y, float z) to scale the text
    // Font.draw to render the text needed -> this.font.draw(PoseStack, float x, float y, int color)

    private void guiInit(PoseStack pPoseStack, float x, float y) {
        pPoseStack.scale(.8f, .8f, .8f);
        this.font.draw(pPoseStack, "Quest Type: ", 1.25F*(x + 124), 1.25F*(y + this.font.lineHeight + 1), colorTranslate(Color.CYAN));
        pPoseStack.scale(1.25f, 1.25f, 1.25f);
    }
    private static int colorTranslate (Color color) {
        int alpha = color.getAlpha();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int rgb = (alpha << 24);
        rgb = rgb | (red << 16);
        rgb = rgb | (green << 8);
        rgb = rgb | (blue);
        return rgb;
    }
    protected void createPageControlButtons() {
        this.nextButton = this.addRenderableWidget(new PageButton(this.leftPos + 224, this.topPos + 144, true, (p_98297_) -> {
            this.pageForward();
        }, this.playTurnSound));
        this.prevButton = this.addRenderableWidget(new PageButton(this.leftPos + 198, this.topPos + 144, false, (p_98287_) -> {
            this.pageBack();
        }, this.playTurnSound));
        this.updateButtonVisibility();
    }
    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }
        this.updateButtonVisibility();
    }
    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }
    protected void updateButtonVisibility() {
        this.nextButton.visible = this.currentPage < this.getNumPages() - 1;
        this.prevButton.visible = this.currentPage > 0;
    }
    protected int getNumPages() {
        switch(currTab){
            case 0:
                return QuestScreensData.numClosedQuests;
            case 1:
                return QuestScreensData.numOpenQuests;
            default:
                return 0;
        }
    }

    @Override
    public void tabChanged(int id){
        TABS[currTab].x-=4;
        TABS[id].x+=4;
    }
}
