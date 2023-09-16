package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.court.ClientCitizenData;
import com.cdogsnappy.snappystuff.network.QuestCreatePacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.mission.*;
import com.cdogsnappy.snappystuff.util.ClientEntityCache;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import java.util.List;
import java.util.UUID;
import com.google.common.collect.Lists;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;
import static com.cdogsnappy.snappystuff.screen.QuestScreensData.ButtonType.*;
public class QuestCreateScreen extends QuestScreen<QuestCreateMenu> {
    private ResourceLocation MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    private ResourceLocation GUITEXTURE = new ResourceLocation(SnappyStuff.MODID,"textures/gui/custom_gui_stuff.png");
    private final List<AbstractButton> editButtons = Lists.newArrayList();
    private int page = 0;
    private int numPages = 0;
    private Object selectedObject;
    private int editingMission = 0;
    private boolean isSearching = false;
    private List<ObjectSelectButton> searchButtons = Lists.newArrayList();
    private List<ObjectSelectButton> playerSearchButtons = Lists.newArrayList();
    private QuestScreensData.ButtonType currentSearchMenu = COLLECT;
    private PageButton prevButton;
    private PageButton nextButton;
    private QuestCreateButton createQuestButton;
    private PageButton backButton;
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
        editButtons.add(this.addRenderableWidget(new QuestCreateMissionButton(this.leftPos + 175,this.topPos + 24,30,18,Component.empty(),this, 0)));
        editButtons.add(this.addRenderableWidget(new QuestCreateMissionButton(this.leftPos + 175,this.topPos + 64,30,18,Component.empty(),this, 1)));
        editButtons.add(this.addRenderableWidget(new QuestCreateMissionButton(this.leftPos + 175,this.topPos + 103,30,18, Component.empty(),this, 2)));
        for(int j = 0; j < 5; ++j){
            for(int i = 0; i < 8; ++i) {
                searchButtons.add(this.addRenderableWidget(new ObjectSelectButton(this.leftPos + 30 + 18*i,this.topPos + 32 + 18*j, 18, 18, null, this)));
                searchButtons.get((j*8)+i).visible = false;
            }
        }

        for(int i = 0; i<8; ++i){
            playerSearchButtons.add(this.addRenderableWidget(new ObjectSelectButton(this.leftPos + 20,this.topPos + 40 + (18*i),80,18,null,this)));
            playerSearchButtons.get(i).visible = false;
        }
        this.nextButton = this.addRenderableWidget(new PageButton(this.leftPos + 219, this.topPos + 138, true, (p_98297_) -> {
            this.pageForward();
        }, true));
        this.prevButton = this.addRenderableWidget(new PageButton(this.leftPos + 195, this.topPos + 138, false, (p_98287_) -> {
            this.pageBack();
        }, true));
        missionTypeButtons = Lists.newArrayList();
        missionTypeButtons.add(this.addRenderableWidget(new Button(this.leftPos + 187,this.topPos + 31,20,20,Component.literal("B"),(b) -> {
            this.currentSearchMenu = QuestScreensData.ButtonType.BLOCK;
            this.resetSearchMenu();
        }
        )));
        missionTypeButtons.add(this.addRenderableWidget(new Button(this.leftPos + 187,this.topPos + 31 + 22,20,20,Component.literal("C"),(b) -> {
            this.currentSearchMenu = QuestScreensData.ButtonType.COLLECT;
            this.resetSearchMenu();
        }
        )));
        missionTypeButtons.add(this.addRenderableWidget(new Button(this.leftPos + 187,this.topPos + 31 + 44,20,20,Component.literal("K"),(b) -> {
            this.currentSearchMenu = KILL;
            this.resetSearchMenu();
        }
        )));
        missionTypeButtons.add(this.addRenderableWidget(new Button(this.leftPos + 187,this.topPos + 31 + 66,20,20,Component.literal("PK"),(b) -> {
            this.currentSearchMenu = PLAYERKILL;
            this.resetSearchMenu();
        }
        )));

        createQuestButton = this.addRenderableWidget(new QuestCreateButton(this.leftPos + 191, this.topPos + 200, 49, 18, Component.empty()));
        backButton = this.addRenderableWidget(new PageButton(this.leftPos + 201, this.topPos + 215, false, (p) -> {
            switchBackToMainMenu();
        },true));
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
        this.isSearching = false;
        makeInvisible(missionTypeButtons);
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
            makeInvisible(searchButtons);
            makeInvisible(playerSearchButtons);
            makeVisible(missionTypeButtons);
            nextButton.visible = true;
            prevButton.visible = true;
            if(searchBox.isChanged()){
                QuestScreensData.refreshList(searchBox.currString,currentSearchMenu);
                refreshPageNum(this);
                page = 0;
            }
            switch(currentSearchMenu){
                case BLOCK:
                    renderButtonSymbols();
                    if(selectedObject != null){drawItem(this.leftPos + 211, this.topPos + 175, new ItemStack(((Block)selectedObject).asItem()));}
                    break;
                case COLLECT:
                    renderButtonSymbols();
                    if(selectedObject != null){drawItem(this.leftPos + 211, this.topPos + 175,(ItemStack)selectedObject);}
                    break;
                case KILL:
                    renderEntityInInventory(this.leftPos + 98f,this.topPos + 115f, 40,0,0);
                    break;
                case PLAYERKILL:
                    renderPlayerButtonSymbols();
                    break;
                default:
                    break;
            }
            this.searchBox.render(pPoseStack,mx,my,fr);
            this.numberBox.render(pPoseStack,mx,my,fr);
        }
        else{
            for(int j = 0; j < 3; ++j){
                renderMission(pPoseStack,this.leftPos + 56,this.topPos + 43+(40*j),110,21,missions[j]);
            }
        }
    }
    @SuppressWarnings("deprecation")
    /**
     * CREDIT: Shadows-of-Fire
     * https://github.com/Shadows-of-Fire/Hostile-Neural-Networks/blob/1.19/src/main/java/shadows/hostilenetworks
     */
    public void renderEntityInInventory(float pPosX, float pPosY, float pScale, float pMouseX, float pMouseY) {
        if(QuestScreensData.filteredTokens.size() == 0){return;}
        LivingEntity pLivingEntity = ClientEntityCache.computeIfAbsent((EntityType)QuestScreensData.filteredTokens.get(page),minecraft.level);
        renderEntityInInventory(pPosX, pPosY, pScale, pMouseX,pMouseY,pLivingEntity);
    }
    private void renderPlayerButtonSymbols(){
        for(int j = 0; j < 8; ++j){
            if(j+8*page >= QuestScreensData.filteredTokens.size()){
                return;
            }
            ObjectSelectButton b = playerSearchButtons.get(j);
            b.visible = true;
            b.target = QuestScreensData.filteredTokens.get(j + 8*page);
            b.setMessage(Component.literal(((ClientCitizenData)b.target).name));
        }

    }
    private void renderButtonSymbols(){
        for(int j = 0; j < 5; ++j){
            for(int i = 0; i < 8; ++i){
                if(((j*8)+i + (40*page)) >= QuestScreensData.filteredTokens.size()){
                    return;
                }
                ObjectSelectButton b = searchButtons.get((j*8)+i);
                b.visible = true;
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

    public class QuestCreateButton extends AbstractButton{


        public QuestCreateButton(int pX, int pY, int pWidth, int pHeight, Component pMessage) {
            super(pX, pY, pWidth, pHeight, pMessage);
        }

        @Override
        public void onPress() {
            if(isSearching){createMission();}
            else{attemptCreateQuest();}
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
            this.blit(pPoseStack,this.x,this.y,0,78,49,18);
        }
    }

    /**
     * Edit buttons
     */
    public class QuestCreateMissionButton extends AbstractButton {

        private boolean selected;
        private QuestCreateScreen qcs;
        private int num;
        public QuestCreateMissionButton(int x, int y, int w, int l, Component text, QuestCreateScreen qcs, int num) {
            super(x,y,w,l,text);
            this.qcs = qcs;
            this.num = num;
        }
        @Override
        public void onPress() {
            makeInvisible(editButtons);
            this.qcs.MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/mission_create_gui.png");
            this.qcs.isSearching = true;
            editingMission = num;
            QuestScreensData.refreshList("", this.qcs.currentSearchMenu);
            refreshPageNum(this.qcs);
            updateButtonVisibility();
            makeInvisible(editButtons);
        }

        public boolean isSelected() { return this.selected; }
        public void setSelected(boolean selected) { this.selected = selected; }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {
        }
        @Override
        public void render(PoseStack pPoseStack, int pX, int pY, float delta){
            if(!this.visible){return;}
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0,GUITEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(pPoseStack,this.x,this.y,0,114,30,18);
        }
    }
    private void refreshPageNum(QuestCreateScreen qcs){
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
        @Override
        public void render(PoseStack pPoseStack, int pX, int pY, float delta){
            if(!this.visible){return;}
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0,GUITEXTURE);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(pPoseStack,this.x,this.y,0,132,18,18);
        }
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
        QuestScreensData.refreshList("",this.currentSearchMenu);
        refreshPageNum(this);
        this.searchBox.setValue("");
        selectedObject = null;
        numberBox.setValue("");
        makeInvisible(searchButtons);
    }
    protected void createMission(){
        if((selectedObject == null && currentSearchMenu != KILL) || (numberBox.getValue().equals("") && currentSearchMenu != PLAYERKILL)){return;}
        switch(currentSearchMenu){
            case COLLECT:
                ItemStack itemStack = new ItemStack(((ItemStack)selectedObject).getItem());
                missions[editingMission] = new CollectMission(itemStack, Integer.parseInt(numberBox.getValue()), minecraft.player.getUUID());
                break;
            case BLOCK:
                missions[editingMission] = new BlockMission((Block)selectedObject, Integer.parseInt(numberBox.getValue()));
                break;
            case KILL:
                missions[editingMission] = new KillMission((EntityType)QuestScreensData.filteredTokens.get(page),Integer.parseInt(numberBox.getValue()));
                break;
            case PLAYERKILL:
                missions[editingMission] = new PlayerKillMission(((ClientCitizenData)selectedObject).playerID, ((ClientCitizenData)selectedObject).name);
                break;
        }
        switchBackToMainMenu();
    }
    protected void attemptCreateQuest(){
        List<Mission> missions = Lists.newArrayList();
        List<ItemStack> rewards = Lists.newArrayList();
        for(Mission m : this.missions){
            if(m != null){
                missions.add(m);
            }
        }
        if(missions.isEmpty()){return;}//quest must have at least one mission
        for(int j = 0; j<5; ++j){//quests could technically be pro-bono, not sure why but go off
            rewards.add(this.menu.slots.get(36 + j).getItem());
            this.menu.slots.get(36 + j).set(ItemStack.EMPTY);
        }
        this.menu.blockEntity.clearRewardSlots();
        SnappyNetwork.sendToServer(new QuestCreatePacket(new ClosedContractQuest(missions,rewards,minecraft.player.getUUID(), Quest.QuestType.PLAYER),this.menu.blockEntity.getBlockPos()));
        this.missions = new Mission[3];
    }
    private static void makeInvisible(List<? extends AbstractButton> buttons){
        buttons.forEach((b) -> {
            b.visible = false;
        });
    }
    private static void makeVisible(List<? extends AbstractButton> buttons){
        buttons.forEach((b) -> {
            b.visible = true;
        });
    }
    public void switchBackToMainMenu(){
        makeInvisible(searchButtons);
        makeInvisible(playerSearchButtons);
        makeInvisible(missionTypeButtons);
        makeVisible(editButtons);
        selectedObject = null;
        numberBox.setValue("");
        isSearching = false;
        MAIN_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_create_block_gui.png");
    }
}
