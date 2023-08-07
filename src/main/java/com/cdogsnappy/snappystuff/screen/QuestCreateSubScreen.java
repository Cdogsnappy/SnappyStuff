package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class QuestCreateSubScreen<C extends QuestCreateMenu, P extends QuestCreateScreen> extends QuestCreateScreen {

    private QuestScreensData.ButtonType type = QuestScreensData.ButtonType.BLOCK;
    private EditBox searchBox;
    private List<MissionSelectButton> missionButtons;
    private int page;

    private static final ResourceLocation QUEST_TEXTURE = new ResourceLocation(SnappyStuff.MODID, "textures/gui/quest_accept_block_gui.png");
    private final P parent;
    private String searchText;
    public QuestCreateSubScreen(P p) {
        super(p.getMenu(),p.getMenu().getPlayerInventory(),p.getTitle());
        parent = p;

    }

    @Override
    protected void init() {
        super.init();
        this.searchBox = new EditBox(this.font, 34, 23, 80, 9, Component.literal("Search..."));
        this.searchBox.setMaxLength(50);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(false);
        this.searchBox.setTextColor(16777215);
        this.addWidget(this.searchBox);
        this.missionButtons = new ArrayList<>();
        this.missionButtons.add(new MissionSelectButton(30,15,Component.literal("Block"),this, QuestScreensData.ButtonType.BLOCK));
        this.missionButtons.add(new MissionSelectButton(60,15,Component.literal("Collect"),this, QuestScreensData.ButtonType.COLLECT));
        this.missionButtons.add(new MissionSelectButton(90,15,Component.literal("Kill"),this, QuestScreensData.ButtonType.KILL));
        this.missionButtons.add(new MissionSelectButton(120,15,Component.literal("Player Kill"),this, QuestScreensData.ButtonType.PLAYERKILL));
        this.missionButtons.forEach((b) -> {
            this.addWidget(b);
        });

    }

    public class MissionSelectButton extends AbstractButton {
        private QuestCreateSubScreen qcs;
        private QuestScreensData.ButtonType buttonType;


        public MissionSelectButton(int x, int y,Component text, QuestCreateSubScreen qcs, QuestScreensData.ButtonType type) {
            super(x, y, 22,22, text);
            this.qcs = qcs;
            this.buttonType = type;
        }

        @Override
        public void onPress() {
            qcs.type = this.buttonType;
            QuestScreensData.refreshList(qcs.searchText,this.buttonType);
            this.changeFocus(true);


        }

        @Override
        public void updateNarration(NarrationElementOutput p_169152_) {

        }

    }
    public class ObjectSelectButton extends AbstractButton {

        private Object target;


        public ObjectSelectButton(int p_93365_, int p_93366_, int p_93367_, int p_93368_, Component p_93369_, Object obj) {
            super(p_93365_, p_93366_, p_93367_, p_93368_, p_93369_);
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
}
