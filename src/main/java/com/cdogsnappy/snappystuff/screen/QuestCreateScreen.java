package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.mission.BlockMission;
import com.cdogsnappy.snappystuff.quest.mission.CollectMission;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
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

import static com.cdogsnappy.snappystuff.screen.QuestScreensData.ButtonType.*;


public class QuestCreateScreen extends AbstractContainerScreen<QuestCreateMenu> {
    private static final Logger LOGGER = LogUtils.getLogger();
    private ResourceLocation MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private List<AbstractButton> editButtons = Lists.newArrayList();
    private int page = 0;
    private int numPages = 0;
    private int count = 1;
    private static final int HIDDEN_POS = 9999;
    private Object selectedObject;
    private int editingMission = 0;
    private boolean isSearching = false;
    private List<ObjectSelectButton> searchButtons = Lists.newArrayList();
    private List<ObjectSelectButton> playerSearchButtons = Lists.newArrayList();
    private QuestScreensData.ButtonType currentSearchMenu = COLLECT;
    private PageButton prevButton;
    private PageButton nextButton;
    private List<Button> missionTypeButtons;
    private QuestSearchBox searchBox;
    private NumberBox numberBox;
    private Mission[] missions = new Mission[3];
    private Minecraft minecraft;

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
            searchButtons.add(new ObjectSelectButton(HIDDEN_POS,HIDDEN_POS,18,18, null, this));
        }
        searchButtons.forEach((b) ->{
            this.addRenderableWidget(b);
        });
        for(int i = 0; i<8; ++i){
            playerSearchButtons.add(new ObjectSelectButton(HIDDEN_POS,HIDDEN_POS,80,18,null,this));
        }
        playerSearchButtons.forEach((b) -> {
            this.addRenderableWidget(b);
        });
        this.nextButton = this.addRenderableWidget(new PageButton(HIDDEN_POS, HIDDEN_POS, true, (p_98297_) -> {
            this.pageForward();
        }, true));
        this.prevButton = this.addRenderableWidget(new PageButton(HIDDEN_POS, HIDDEN_POS, false, (p_98287_) -> {
            this.pageBack();
        }, true));
        missionTypeButtons = Lists.newArrayList();
        missionTypeButtons.add(new Button(HIDDEN_POS,HIDDEN_POS,20,20,Component.literal("B"),(b) -> {
            this.currentSearchMenu = QuestScreensData.ButtonType.BLOCK;
            this.resetSearchMenu();
        }
        ));
        missionTypeButtons.add(new Button(HIDDEN_POS,HIDDEN_POS,20,20,Component.literal("C"),(b) -> {
            this.currentSearchMenu = QuestScreensData.ButtonType.COLLECT;
            this.resetSearchMenu();
        }
        ));
        missionTypeButtons.add(new Button(HIDDEN_POS,HIDDEN_POS,20,20,Component.literal("K"),(b) -> {
            this.currentSearchMenu = KILL;
            this.resetSearchMenu();
        }
        ));
        missionTypeButtons.add(new Button(HIDDEN_POS,HIDDEN_POS,20,20,Component.literal("PK"),(b) -> {
            this.currentSearchMenu = PLAYERKILL;
            this.resetSearchMenu();
        }
        ));
        missionTypeButtons.forEach((b) ->{
            this.addRenderableWidget(b);
        });
        this.searchBox = new QuestSearchBox(this.font, this.leftPos + 30, this.topPos + 21, 80, 9, Component.literal("Search..."));
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(true);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(16777215);
        this.addWidget(this.searchBox);
        numberBox = new NumberBox(this.font, this.leftPos + 204, this.topPos + 156, 30, 15, Component.empty());
        this.numberBox.setMaxLength(3);
        this.numberBox.setBordered(true);
        this.numberBox.setVisible(true);
        this.numberBox.setTextColor(15777215);
        this.addWidget(numberBox);
        minecraft = Minecraft.getInstance();
    }


    @Override
    protected void renderBg(PoseStack pPoseStack, float fr, int mx, int my) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, MAIN_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        if(isSearching){
            searchButtons.forEach((b) ->{
                b.x = HIDDEN_POS;
                b.y = HIDDEN_POS;
            });
            playerSearchButtons.forEach((b) -> {
                b.x = HIDDEN_POS;
                b.y = HIDDEN_POS;
            });
            if(searchBox.isChanged()){
                QuestScreensData.refreshList(searchBox.currString,currentSearchMenu);
            }
            switch(currentSearchMenu){
                case BLOCK,COLLECT:
                    renderButtonSymbols();
                    break;
                case KILL:
                    renderEntity(pPoseStack);
                    break;
                case PLAYERKILL:
                    renderPlayerButtonSymbols();
                    break;
                default:
                    break;
            }
            this.searchBox.render(pPoseStack,mx,my,fr);
            this.numberBox.render(pPoseStack,mx,my,fr);
            this.nextButton.x = this.leftPos + 219;
            this.nextButton.y = this.topPos + 138;
            this.prevButton.x = this.leftPos + 195;
            this.prevButton.y = this.topPos + 138;
            for(int j = 0; j < 4; ++j){
                this.missionTypeButtons.get(j).x = this.leftPos + 187;
                this.missionTypeButtons.get(j).y = this.topPos + 31 + 22*j;
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
    private void renderPlayerButtonSymbols(){
        for(int j = 0; j < 8; ++j){
            if(j+8*page >= QuestScreensData.filteredTokens.size()){
                return;
            }
            ObjectSelectButton b = playerSearchButtons.get(j);
            b.x = this.leftPos + 20;
            b.y = this.topPos + 40 + (18*j);
            b.target = QuestScreensData.filteredTokens.get(j + 8*page);
            b.setMessage(Component.literal((String)b.target));

        }

    }
    private void renderButtonSymbols(){
        for(int j = 0; j < 5; ++j){
            for(int i = 0; i < 8; ++i){
                if(((j*8)+i + (40*page)) >= QuestScreensData.filteredTokens.size()){
                    return;
                }
                ObjectSelectButton b = searchButtons.get((j*8)+i);
                b.x = this.leftPos + 30 + 18*i;
                b.y = this.topPos + 32 + 18*j;
                switch(this.currentSearchMenu){
                    case COLLECT:
                        ItemStack item = new ItemStack((Item)QuestScreensData.filteredTokens.get(((j*8)+i) + (40*page)));
                        b.target = item;
                        drawItem((this.leftPos+30+18*i) + 1, (this.topPos + 32 + 18*j) + 1,item);
                        break;
                    case BLOCK:
                        b.target = QuestScreensData.filteredTokens.get(((j*8)+i) + (40*page));
                        ItemStack block = new ItemStack(((Block)QuestScreensData.filteredTokens.get(((j*8)+i) + (40*page))).asItem());
                        drawItem((this.leftPos+30+18*i) + 1, (this.topPos + 32 + 18*j) + 1, block);
                        break;


                }


            }
        }
    }

    /**
     * Edit buttons
     */
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
                refreshPageNum(this.qcs);
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
    private static void refreshPageNum(QuestCreateScreen qcs){
        switch(qcs.currentSearchMenu){
            case COLLECT, BLOCK:
                qcs.numPages = QuestScreensData.filteredTokens.size()/40;
                if(QuestScreensData.filteredTokens.size() % 40 > 0){
                    qcs.numPages++;
                }
                break;
            case KILL:
                qcs.numPages = QuestScreensData.filteredTokens.size();
                break;
            case PLAYERKILL:
                qcs.numPages = QuestScreensData.filteredTokens.size()/16;
                if(QuestScreensData.filteredTokens.size() % 16 > 0){
                    qcs.numPages++;
                }
                break;
        }
    }

    public class ObjectSelectButton extends AbstractButton {

        private Object target;
        private QuestCreateScreen qcs;


        public ObjectSelectButton(int p_93365_, int p_93366_, int p_93367_, int p_93368_, Object obj, QuestCreateScreen qcs) {
            super(p_93365_, p_93366_, p_93367_, p_93368_, Component.empty());
            this.target = obj;
            this.qcs = qcs;
        }

        @Override
        public void onPress() {
            this.qcs.selectedObject = this.target;
        }
        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {}
        @Override
        public void renderToolTip(PoseStack p_93653_, int p_93654_, int p_93655_) {
            switch(qcs.currentSearchMenu){
                case COLLECT:
                    ItemStack i = (ItemStack)target;
                    qcs.minecraft.screen.renderComponentTooltip(p_93653_, qcs.minecraft.screen.getTooltipFromItem(i), p_93654_,  p_93655_, i);
                    break;
                case BLOCK:
                    ItemStack bi = new ItemStack(((Block)target).asItem());
                    qcs.minecraft.screen.renderComponentTooltip(p_93653_, qcs.minecraft.screen.getTooltipFromItem(bi), p_93654_,  p_93655_, bi);
                    break;
                default:
                    break;
            }
        }
    }
    public void drawItem(int x, int y, ItemStack is) {
        this.itemRenderer.blitOffset = 100.0F;
        this.itemRenderer.renderAndDecorateItem(is, x, y);
        this.itemRenderer.blitOffset = 0.0F;
    }
    protected void pageForward() {
        if (this.page < this.numPages - 1) {
            ++this.page;
        }

        this.updateButtonVisibility();
    }
    protected void pageBack() {
        if (this.page > 0) {
            --this.page;
        }

        this.updateButtonVisibility();
    }
    protected void updateButtonVisibility() {
        this.nextButton.visible = this.page < this.numPages - 1;
        this.prevButton.visible = this.page > 0;
    }
    protected void resetSearchMenu(){
        page = 0;
        refreshPageNum(this);
        QuestScreensData.refreshList("",this.currentSearchMenu);
        this.searchBox.setValue("");
    }
    protected void createMission(){
        switch(currentSearchMenu){
            case COLLECT:
                ItemStack itemStack = new ItemStack(((ItemStack)selectedObject).getItem(),count);
                missions[editingMission] = new CollectMission(itemStack);
                break;
            case BLOCK:
                missions[editingMission] = new BlockMission((Block)selectedObject, count);
        }
    }
}
