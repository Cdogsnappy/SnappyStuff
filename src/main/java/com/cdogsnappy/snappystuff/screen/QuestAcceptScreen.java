package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.blocks.QuestAcceptBlock;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.quest.Mission;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestAcceptScreen extends AbstractContainerScreen<QuestAcceptMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_accept_block_gui.png");
    private final int verticalPadding = 7;
    private final int horizontalPadding = 9;
    private Font font1;
    private PageButton prevButton;
    private PageButton nextButton;
    private final boolean playTurnSound = true;
    private int currentPage = 0;
    private ArrayList<String> temp;
    private final List<QuestAcceptScreen.QuestButtons> buttonList = Lists.newArrayList();
    public QuestAcceptScreen(QuestAcceptMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageWidth = 256;
        this.imageHeight = 234;
        this.titleLabelX = 12;
        this.titleLabelY = 8;
        this.inventoryLabelX = 22;
        this.inventoryLabelY = this.imageHeight - 104;
        temp = new ArrayList<String>();
        temp.add("Quest 1");
        temp.add("Quest 2");
    }

    @Override
    protected void init() {
        super.init();
        createPageControlButtons();
        this.addButton(new QuestAcceptScreen.QuestAcceptConfirmButton(this.width / 2, this.height / 2));
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
            super(x, y, 22, 22, component);
        }
        public void renderButton(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, new ResourceLocation("textures/gui/quest_accept_block_gui"));
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
    class QuestAcceptMenuButton extends QuestAcceptScreenButton {
        private TextureAtlasSprite sprite;
        private Component tooltip;
        QuestAcceptMenuButton(int x, int y) {
            super(x, y);
        }
        public void onPress() {
            //change menus
        }
        public void renderToolTip(PoseStack pPoseStack, int mouseX, int mouseY) {
            QuestAcceptScreen.this.renderTooltip(pPoseStack, this.tooltip, mouseX, mouseY);
        }
        protected void renderIcon(PoseStack pPoseStack) {
            RenderSystem.setShaderTexture(0, this.sprite.atlas().location());
            blit(pPoseStack, this.x + 2, this.y + 2, this.getBlitOffset(), 18, 18, this.sprite);
        }
        public void updateStatus(int status) {
            //set menu button to active
        }
    }
    class QuestAcceptConfirmButton extends QuestAcceptScreen.QuestAcceptSpriteScreenButton {
        public QuestAcceptConfirmButton(int x, int y) {
            super(x, y, 90, 220, CommonComponents.GUI_DONE);
        }
        public void onPress() {
            //Send packets
            QuestAcceptScreen.this.minecraft.player.closeContainer();
        }
        public void updateStatus(int status) {
            //Remove quest from thinger
        }
    }
    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    //use PoseStack.scale -> PoseStack.scale(float x, float y, float z) to scale the text
    // Font.draw to render the text needed -> this.font.draw(PoseStack, float x, float y, int color)
    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
        Font font1 = this.font;
        int fontHeight = this.font.lineHeight + 1;
        int containerWidth = 236;
        int containerHeight = 74;
        int x = this.width / 2 - containerWidth;
        int y = this.height / 2 - containerHeight;
        guiInit(pPoseStack, x, y);
        this.font.draw(pPoseStack, mouseX + ", " + mouseY, mouseX, mouseY, 0); //shows mouse coordinate position
        if (temp.isEmpty())
            this.font.draw(pPoseStack, "No Quests!", x + 80 - (float)(this.font.width("No Quests!")/2), y + 40, 0);
        else {
            this.font.draw(pPoseStack, temp.get(currentPage), x + 120- (float)(this.font.width("Quest #")/2), y + 50, 0);
        }
    }

    private void guiInit(PoseStack pPoseStack, float x, float y) {
        pPoseStack.scale(.8f, .8f, .8f);
        this.font.draw(pPoseStack, "Quest Type: ", 1.25F*(x + 124), 1.25F*(y + this.font.lineHeight + 1), colorTranslate(Color.CYAN));
        pPoseStack.scale(1.25f, 1.25f, 1.25f);
    }

    private int colorTranslate (Color color) {
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
        int i = this.width/2;
        int j = this.height/2 - 30;
        this.nextButton = this.addRenderableWidget(new PageButton(i + 50, j, true, (p_98297_) -> {
            this.pageForward();
        }, this.playTurnSound));
        this.prevButton = this.addRenderableWidget(new PageButton(i - 74, j, false, (p_98287_) -> {
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
        return temp.size();
    }
}
