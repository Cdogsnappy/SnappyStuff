package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.network.QuestRequestPacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.OpenContractQuest;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class QuestOverviewScreen extends QuestScreen<QuestOverviewMenu> {
    private int currPage;
    private Tab[] TABS = new Tab[3];
    private ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");

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
        currTab = 0;
        TABS[0] = this.addWidget(new Tab(this.leftPos + this.imageWidth - 7, this.topPos + 30, null, 0, new ItemStack(Items.IRON_PICKAXE), this));
        TABS[1] = this.addWidget(new Tab(this.leftPos + this.imageWidth - 11, this.topPos + 60, null, 1, new ItemStack(Items.BOOK), this));
        TABS[2] = this.addWidget(new Tab(this.leftPos + this.imageWidth - 11, this.topPos + 90, null, 2, new ItemStack(Items.EMERALD), this));
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
        switch (currTab) {
            case 0:
                this.menu.changeRewardVisibility(false);
                break;
            case 1:
                this.menu.changeRewardVisibility(false);
                break;
            case 2:
                this.menu.changeRewardVisibility(true);
                break;
        }
    }

    @Override
    public void tabChanged(int id) {
        TABS[currTab].x -= 4;
        TABS[id].x += 4;
        currPage = 0;
        if (id == 2) {
            TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_rewards_gui.png");
        } else {
            TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
        }
    }

    public int getPageNum() {
        switch (currTab) {
            case 0:
                return QuestScreensData.playerQuests.size();
            case 1:
                return QuestScreensData.playerCreatedQuests.size();
            case 2:
                if (QuestScreensData.playerCreatedQuests.size() % 60 > 0) {
                    return QuestScreensData.playerCreatedQuests.size() / 60 + 1;
                } else {
                    return QuestScreensData.playerCreatedQuests.size();
                }
            default:
                return -1;//Case that makes no sense
        }
    }
}

