package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.network.*;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
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

import java.util.List;

public class QuestOverviewScreen extends QuestScreen<QuestOverviewMenu> {
    private int clearTimer = 10;
    private int completeCooldown = 0;
    private int currentPage;
    private Tab[] TABS = new Tab[3];
    private ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private ResourceLocation GUITEXTURE = new ResourceLocation(SnappyStuff.MODID,"textures/gui/custom_gui_stuff.png");
    private CancelButton cancelButton;
    private ClaimButton claimButton;
    private CompleteButton completeButton;
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
        completeButton = this.addRenderableWidget(new CompleteButton(this.leftPos + 193, this.topPos + 180, 45, 18));
        claimButton = this.addRenderableWidget(new ClaimButton(this.leftPos + 191, this.topPos + 200, 40, 18, Component.empty()));
        completeButton.visible = !QuestScreensData.questData.acceptedQuests.isEmpty();
        claimButton.visible = false;
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
                    cancelButton.visible = false;
                    break;
                }
                ClosedContractQuest cq = QuestScreensData.questData.acceptedQuests.get(currentPage);
                for(int j = 0; j < cq.missions.size(); ++j){
                    renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43+(40*j), cq.missions.get(j));
                }
                this.font.draw(pPoseStack, "Requestor:", this.leftPos + 192, this.topPos + 140, 0);
                this.font.draw(pPoseStack, CitizenData.getPlayerName(cq.requestor), this.leftPos + 192, this.topPos + 146,0);
                showQuestRewards(cq);
                break;
            case 1:
                if(QuestScreensData.questData.createdQuests.isEmpty()){
                    this.font.draw(pPoseStack, "No Quests!", x + 80 - (float) (this.font.width("No Quests!") / 2), y + 40, 0);
                    cancelButton.visible = false;
                    break;
                }
                ClosedContractQuest crq = QuestScreensData.questData.createdQuests.get(currentPage);
                for(int j = 0; j < crq.missions.size(); ++j){
                    renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43+(40*j), crq.missions.get(j));
                }
                this.font.draw(pPoseStack, "Questor:", this.leftPos + 192, this.topPos + 140, 0);
                if(crq.questor == null){
                    this.font.draw(pPoseStack, "Nobody!", this.leftPos + 192, this.topPos + 146,0);
                    cancelButton.visible = true;
                }
                else{
                    this.font.draw(pPoseStack, CitizenData.getPlayerName(crq.questor), this.leftPos + 192, this.topPos + 146,0);
                    cancelButton.visible = false;
                }
                showQuestRewards(crq);
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
        switch(id){
            case 2:
                TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_rewards_gui.png");
                menu.checkOrAddRewardSlots();
                cancelButton.visible = false;
                claimButton.visible = true;
                completeButton.visible = false;
                break;
            case 1:
                TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
                menu.checkOrAddDisplaySlots();
                cancelButton.visible = true;
                claimButton.visible = false;
                completeButton.visible = false;
                break;
            case 0:
                TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
                menu.checkOrAddDisplaySlots();
                cancelButton.visible = true;
                claimButton.visible = false;
                completeButton.visible = !QuestScreensData.questData.acceptedQuests.isEmpty();
                break;
            default:
                break;

        }
        currTab = id;
        clearQuestRewards();
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
                    return QuestScreensData.questData.rewards.size() / 60;
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
    public void showQuestRewards(Quest q){
        for(int i = 0; i < 5; ++i){
            if(i >= q.rewards.size()){return;}
            this.menu.slots.get(36+i).set(q.rewards.get(i));
        }
    }
    public void clearQuestRewards(){
        for(int i = 0; i < 5; ++i){
            this.menu.slots.get(36+i).set(ItemStack.EMPTY);
        }
    }

    public class CancelButton extends AbstractButton {

        public CancelButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
            super(pX, pY, pWidth, pHeight, pMessage);
        }
        @Override
        public void onPress() {
            ClosedContractQuest cq = null;
            if(currTab == 0){cq = QuestScreensData.questData.acceptedQuests.get(currentPage);}
            else{cq = QuestScreensData.questData.createdQuests.get(currentPage);}
            minecraft.player.sendSystemMessage(Component.literal("check1"));
            SnappyNetwork.sendToServer(new QuestCancelPacket(cq));
            if(currentPage >= getNumPages()-1 && currentPage != 0){--currentPage;}
            clearQuestRewards();

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
    public class ClaimButton extends AbstractButton {


        public ClaimButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
            super(pX, pY, pWidth, pHeight, pMessage);
        }
        @Override
        public void onPress() {
            SnappyNetwork.sendToServer(new RewardDistributionRequestPacket());
            menu.clearRewardSlots();
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
            this.blit(pPoseStack,this.x,this.y,0,190,40,18);
        }
    }
    public class CompleteButton extends AbstractButton {
        public CompleteButton(int pX, int pY, int pWidth, int pHeight) {
            super(pX, pY, pWidth, pHeight, Component.empty());
        }
        @Override
        public void onPress() {
            if(completeCooldown > 0){return;}
            SnappyNetwork.sendToServer(new AttemptCompleteQuestPacket(QuestScreensData.questData.acceptedQuests.get(currentPage)));
            completeCooldown = 60;
            if(currentPage >= getNumPages()-1 && currentPage != 0){--currentPage;}
            clearQuestRewards();
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
            this.blit(pPoseStack,this.x,this.y,0,208,45,18);
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

    @Override
    public void containerTick(){

        completeCooldown = Math.max(0,completeCooldown-1);
        if(currTab == 2 && --clearTimer < 0){
            menu.clearRewardSlots();
            clearTimer = 10;
        }
    }
}

