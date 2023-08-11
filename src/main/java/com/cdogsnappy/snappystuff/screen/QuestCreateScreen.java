package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

import javax.swing.text.html.parser.Entity;

import static com.cdogsnappy.snappystuff.screen.QuestScreensData.ButtonType.COLLECT;


public class QuestCreateScreen extends AbstractContainerScreen<QuestCreateMenu> {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected List<Mission> missions;
    private ResourceLocation MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private List<AbstractButton> editButtons = Lists.newArrayList();
    private int page = 0;
    private int numPages = 0;
    private static final int HIDDEN_POS = 9999;
    private Object selectedObject;
    private int counter = 1;
    private boolean isSearching = false;
    private List<ObjectSelectButton> searchButtons = Lists.newArrayList();
    private QuestScreensData.ButtonType currentSearchMenu = COLLECT;

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
        QuestScreensData.refreshList("",COLLECT);
        super.init();
        editButtons.add(new QuestCreateMissionButton(this.leftPos + 178,this.topPos + 26,27,13,Component.literal("EDIT"),this));
        editButtons.add(new QuestCreateMissionButton(this.leftPos + 178,this.topPos + 66,27,13,Component.literal("EDIT"),this));
        editButtons.add(new QuestCreateMissionButton(this.leftPos + 178,this.topPos + 105,27,13, Component.literal("EDIT"),this));
        this.editButtons.forEach((b) ->{
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
        renderButtonSymbols();
        if(isSearching){
            switch(currentSearchMenu){
                case BLOCK,COLLECT:
                    renderButtonSymbols();
                    break;
                case KILL:
                    renderEntity(pPoseStack);
                    break;
                case PLAYERKILL:
                    break;
                default:
                    break;
            }
        }
    }
    private void renderEntity(PoseStack matrixStack){
        matrixStack.pushPose();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        matrixStack.translate(20, 24, 50F);
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            dispatcher.setRenderShadow(false);
            dispatcher.render(((EntityType<?>)QuestScreensData.filteredTokens.get(page)).create(Minecraft.getInstance().level), 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack, buffer, 15728880);
            buffer.endBatch();
        } catch (Exception e) {
        }
        dispatcher.setRenderShadow(true);
        Lighting.setupFor3DItems();
    }
    private void renderButtonSymbols(){
        searchButtons.forEach((b) ->{
            b.x = HIDDEN_POS;
            b.y = HIDDEN_POS;
        });
        int numButtons = 40;
        if(page == numPages-1){
            numButtons = QuestScreensData.filteredTokens.size() % 40;
        }
        for(int j = 0; j < 5; ++j){
            for(int i = 0; i < 8; ++i){
                if((j*5)+i >= numButtons){
                    return;
                }
                ObjectSelectButton b = searchButtons.get((j*5)+i);
                b.x = this.leftPos + 30 + 18*i;
                b.y = this.topPos + 32 + 18*j;
                switch(this.currentSearchMenu){
                    case COLLECT:
                        ItemStack item = new ItemStack((Item)QuestScreensData.filteredTokens.get(((j*5)+i) + (40*page)));
                        b.target = item;
                        drawItem((this.leftPos+30+18*i) + 1, (this.topPos + 32 + 18*j) + 1,item);
                        break;
                    case BLOCK:
                        b.target = QuestScreensData.filteredTokens.get(((j*5)+i) + (40*page));
                        ItemStack block = new ItemStack(((Block)QuestScreensData.filteredTokens.get(((j*5)+i) + (40*page))).asItem());
                        drawItem((this.leftPos+30+18*i) + 1, (this.topPos + 32 + 18*j) + 1, block);
                        break;
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
            this.qcs.editButtons.forEach((b) ->{
                b.x = HIDDEN_POS;
                b.y = HIDDEN_POS;
            });
            this.qcs.MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/mission_create_gui.png");
            if(this.qcs.currentSearchMenu == COLLECT || this.qcs.currentSearchMenu == QuestScreensData.ButtonType.BLOCK){
                QuestScreensData.refreshList("", this.qcs.currentSearchMenu);
                switch(this.qcs.currentSearchMenu){
                    case COLLECT, BLOCK:
                        this.qcs.numPages = QuestScreensData.filteredTokens.size()/40;
                        if(QuestScreensData.filteredTokens.size() % 40 > 0){
                            this.qcs.numPages++;
                        }
                        return;
                    case KILL:
                        this.qcs.numPages = QuestScreensData.filteredTokens.size();
                        return;
                    case PLAYERKILL:
                        this.qcs.numPages = QuestScreensData.filteredTokens.size()/16;
                        if(QuestScreensData.filteredTokens.size() % 16 > 0){
                            this.qcs.numPages++;
                        }
                        return;
                }
                this.qcs.isSearching = true;
                this.qcs.renderButtonSymbols();
            }
        }

        public boolean isSelected() { return this.selected; }
        public void setSelected(boolean selected) { this.selected = selected; }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }
    }
    public class PageButton extends AbstractButton {

        private boolean selected;
        private QuestCreateScreen qcs;
        private boolean forward;
        public PageButton(int x, int y, int w, int l, Component text, QuestCreateScreen qcs, boolean forward) {
            super(x,y,w,l,text);
            this.qcs = qcs;
            this.forward = forward;
        }
        @Override
        public void onPress() {
            if((this.qcs.page == this.qcs.numPages - 1 && forward) || (this.qcs.page == 0 && !forward)){
                return;
            }
            if(forward){
                this.qcs.page++;
            }
            else{
                this.qcs.page--;
            }
            switch(this.qcs.currentSearchMenu){
                case BLOCK:
                    this.qcs.renderButtonSymbols();
                case COLLECT:
                    this.qcs.renderButtonSymbols();
                default:
                    return;
            }
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
    }

    public void drawItem(int x, int y, ItemStack is) {
        this.itemRenderer.blitOffset = 100.0F;
        this.itemRenderer.renderAndDecorateItem(is, x, y);
        this.itemRenderer.blitOffset = 0.0F;
    }
}
