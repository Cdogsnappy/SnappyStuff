package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.network.PlayerQuestDataRequestPacket;
import com.cdogsnappy.snappystuff.network.QuestCancelPacket;
import com.cdogsnappy.snappystuff.network.QuestRequestPacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

public class QuestOverviewScreen extends QuestScreen<QuestOverviewMenu> {
    private int currentPage;
    private Tab[] TABS = new Tab[3];
    private ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private CancelButton cancelButton;
    private PageButton nextButton;
    private PageButton prevButton;

    public QuestOverviewScreen(QuestOverviewMenu menu, Inventory inventory, Component component) {
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
        SnappyNetwork.sendToServer(new PlayerQuestDataRequestPacket());
        currTab = 0;
        TABS[0] = this.addWidget(new Tab(this.leftPos + this.imageWidth - 7, this.topPos + 30, null, 0, new ItemStack(Items.IRON_PICKAXE), this));
        TABS[1] = this.addWidget(new Tab(this.leftPos + this.imageWidth - 11, this.topPos + 60, null, 1, new ItemStack(Items.BOOK), this));
        TABS[2] = this.addWidget(new Tab(this.leftPos + this.imageWidth - 11, this.topPos + 90, null, 2, new ItemStack(Items.EMERALD), this));
        cancelButton = this.addRenderableWidget(new CancelButton(this.leftPos + 191, this.topPos + 200, 49, 18, Component.empty()));
        createPageControlButtons();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        for (int i = 0; i < 3; ++i) {
            TABS[i].render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        switch(currTab){
            case 0:
                if(QuestScreensData.questData.acceptedQuests.isEmpty()){
                    this.font.draw(pPoseStack, "No Quests!", x + 80 - (float) (this.font.width("No Quests!") / 2), y + 40, 0);
                    break;
                }
                ClosedContractQuest cq = QuestScreensData.questData.acceptedQuests.get(currentPage);
                for(int j = 0; j < cq.missions.size(); ++j){
                    renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43+(40*j),110,21, cq.missions.get(j));
                }
                this.font.draw(pPoseStack, "Requestor:", this.leftPos + 192, this.topPos + 140, 0);
                this.font.draw(pPoseStack, CitizenData.getPlayerName(cq.requestor), this.leftPos + 192, this.topPos + 146,0);
                break;
            case 1:
                if(QuestScreensData.questData.createdQuests.isEmpty()){
                    this.font.draw(pPoseStack, "No Quests!", x + 80 - (float) (this.font.width("No Quests!") / 2), y + 40, 0);
                    break;
                }
                ClosedContractQuest crq = QuestScreensData.questData.createdQuests.get(currentPage);
                for(int j = 0; j < crq.missions.size(); ++j){
                    renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43+(40*j),110,21, crq.missions.get(j));
                }
                this.font.draw(pPoseStack, "Accepted by:", this.leftPos + 192, this.topPos + 140, 0);
                if(crq.questor == null){
                    this.font.draw(pPoseStack, "Nobody yet!", this.leftPos + 192, this.topPos + 146,0);
                    cancelButton.visible = true;
                }
                else{
                    this.font.draw(pPoseStack, CitizenData.getPlayerName(crq.questor), this.leftPos + 192, this.topPos + 146,0);
                    cancelButton.visible = false;
                }
                break;
            case 2:
                fillRewards();
                break;

        }
    }

    @Override
    public void tabChanged(int id) {
        TABS[currTab].x -= 4;
        TABS[id].x += 4;
        currentPage = 0;
        if (id == 2) {
            TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_rewards_gui.png");
            this.menu.checkOrAddRewardSlots();
        } else {
            TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
            this.menu.checkOrAddDisplaySlots();
        }
        currTab = id;
    }

    public int getNumPages() {
        switch (currTab) {
            case 0:
                return QuestScreensData.questData.acceptedQuests.size();
            case 1:
                return QuestScreensData.questData.createdQuests.size();
            case 2:
                if (QuestScreensData.questData.rewards.size() % 60 > 0) {
                    return QuestScreensData.questData.rewards.size() / 60 + 1;
                } else {
                    return QuestScreensData.questData.rewards.size();
                }
            default:
                return -1;//Case that makes no sense
        }
    }

    public void fillRewards(){
        List<ItemStack> rewards = QuestScreensData.questData.rewards;
        int bound  = Math.min(60,rewards.size() - (currentPage)*60);
        for(int i = 0; i < bound; ++i){
            this.menu.slots.get(36+i).set(rewards.get(currentPage*60 + i));
        }
    }

    public class CancelButton extends AbstractButton {
        private ResourceLocation GUITEXTURE = new ResourceLocation(SnappyStuff.MODID,"textures/gui/custom_gui_stuff.png");

        public CancelButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
            super(pX, pY, pWidth, pHeight, pMessage);
        }
        @Override
        public void onPress() {
            ClosedContractQuest cq = null;
            if(currTab == 0){cq = QuestScreensData.questData.acceptedQuests.get(currentPage);}
            else{cq = QuestScreensData.questData.createdQuests.get(currentPage);}
            SnappyNetwork.sendToServer(new QuestCancelPacket(cq));
        }
        @Override
        public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

        }
        @Override
        public void render(PoseStack pPoseStack, int pX, int pY, float delta){
            if(!this.visible){return;}
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0,GUITEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(pPoseStack,this.x,this.y,0,60,49,18);
        }
    }
    protected void createPageControlButtons() {
        this.nextButton = this.addRenderableWidget(new PageButton(this.leftPos + 214, this.topPos + 154, true, (p_98297_) -> {
            this.pageForward();
        }, true));
        this.prevButton = this.addRenderableWidget(new PageButton(this.leftPos + 188, this.topPos + 154, false, (p_98287_) -> {
            this.pageBack();
        }, true));
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
}

