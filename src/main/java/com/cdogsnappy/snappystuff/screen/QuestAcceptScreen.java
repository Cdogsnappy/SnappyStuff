package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.network.QuestAcceptPacket;
import com.cdogsnappy.snappystuff.network.QuestRequestPacket;
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
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.List;

public class QuestAcceptScreen extends QuestScreen<QuestAcceptMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private Font font1;
    private PageButton prevButton;
    private PageButton nextButton;
    private final boolean playTurnSound = true;
    private int currentPage = 0;
    private QuestAcceptConfirmButton confirmButton;
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
        SnappyNetwork.sendToServer(new QuestRequestPacket(0,false));
        currTab = 0;
        confirmButton = this.addRenderableWidget(new QuestAcceptScreen.QuestAcceptConfirmButton(this.leftPos + 195, this.topPos + 198, this));
        TABS[0] = this.addWidget(new Tab(this.leftPos + this.imageWidth-7,this.topPos + 30,null,0, new ItemStack(Items.IRON_PICKAXE), this));
        TABS[1] = this.addWidget(new Tab(this.leftPos + this.imageWidth-11,this.topPos+60,null,1, new ItemStack(ModItems.DEMON_BLADE.get()), this));
        createPageControlButtons();
        updateButtonVisibility();
    }

    private <T extends AbstractWidget & QuestAcceptScreen.QuestButtons> AbstractWidget addButton(T button) {
        this.addRenderableWidget(button);
        this.buttonList.add(button);
        return button;
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
        private QuestAcceptScreen q;
        public QuestAcceptConfirmButton(int x, int y, QuestAcceptScreen q) {
            super(x, y, 90, 220, Component.literal("ACCEPT"));
            this.q = q;
        }
        public void onPress() {
            if(QuestScreensData.questScreenDisplay == null){return;}
            Quest q = QuestScreensData.questScreenDisplay;
            QuestScreensData.questScreenDisplay = null;
            clearRewards();
            SnappyNetwork.sendToServer(new QuestAcceptPacket((ClosedContractQuest)q));
            if(currentPage >= getNumPages()-1 && currentPage != 0){--currentPage;}//Weird code here, but we just removed a quest from the list of quests, so there is now length-1-1 quests.
            //Send packets
        }
        @Override
        public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
            if(this.q.currTab == 1){return;}
            super.renderButton(pPoseStack,mouseX,mouseY,delta);
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
        if ((QuestScreensData.numOpenQuests == 0 && currTab == 1) || (QuestScreensData.numClosedQuests == 0 && currTab == 0) || QuestScreensData.questScreenDisplay == null) {
            this.font.draw(pPoseStack, "No Quests!", x + 80 - (float) (this.font.width("No Quests!") / 2), y + 40, 0);
        }
        else {
            this.font.draw(pPoseStack, "Quest # " + (currentPage+1), this.leftPos + 192, this.topPos + 132, 0);
            switch(currTab){
                case 0:
                    this.font.draw(pPoseStack, "Requestor:", this.leftPos + 192, this.topPos + 140, 0);
                    this.font.draw(pPoseStack, CitizenData.getPlayerName(QuestScreensData.questScreenDisplay.requestor), this.leftPos + 192, this.topPos + 146,0);
                    for(int j = 0; j < ((ClosedContractQuest)QuestScreensData.questScreenDisplay).missions.size(); ++j){renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43+(40*j),110,21,((ClosedContractQuest)QuestScreensData.questScreenDisplay).missions.get(j));}
                    break;
                case 1:
                    renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43,110,21,((OpenContractQuest)QuestScreensData.questScreenDisplay).mission);
                    break;
            }
            renderRewards();
        }
    }

    private void renderRewards(){
        List<ItemStack> rewards = QuestScreensData.questScreenDisplay.rewards;
        if(rewards.size() == 0){return;}
        for(int i = 0; i < rewards.size(); ++i){
            this.menu.slots.get(i+36).set(rewards.get(i));
        }
    }
    private void clearRewards(){for(int j = 0; j < 5; ++j){this.menu.slots.get(j+36).set(ItemStack.EMPTY);}}

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
        this.nextButton = this.addRenderableWidget(new PageButton(this.leftPos + 214, this.topPos + 154, true, (p_98297_) -> {
            this.pageForward();
        }, this.playTurnSound));
        this.prevButton = this.addRenderableWidget(new PageButton(this.leftPos + 188, this.topPos + 154, false, (p_98287_) -> {
            this.pageBack();
        }, this.playTurnSound));
        this.updateButtonVisibility();
    }
    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
            SnappyNetwork.sendToServer(new QuestRequestPacket(currentPage,this.currTab == 1));
            clearRewards();
        }
        this.updateButtonVisibility();
    }
    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
            SnappyNetwork.sendToServer(new QuestRequestPacket(currentPage,this.currTab == 1));
            clearRewards();
        }

        this.updateButtonVisibility();
    }
    protected void updateButtonVisibility() {
        this.nextButton.visible = this.currentPage < this.getNumPages() - 1;
        this.prevButton.visible = this.currentPage > 0;

        if(QuestScreensData.questScreenDisplay == null || QuestScreensData.questScreenDisplay.requestor.equals(minecraft.player.getUUID())){
            this.confirmButton.visible = false;
        }
        else{this.confirmButton.visible = true;}


    }
    protected void syncPageNumber(){
        if(currentPage > getNumPages() - 1){currentPage = getNumPages() - 1;}//Handles desync due to other players accepting quests and changing the quest pool while you are viewing the final quest.
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
        clearRewards();
    }
}
