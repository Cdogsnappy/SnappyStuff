package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class Tab extends AbstractButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SnappyStuff.MODID,"textures/gui/tab_button.png");
    private final int id;
    private final ItemStack icon;
    QuestScreen parent;

    public Tab(int p_93367_, int p_93368_, Component p_93369_, int id, ItemStack icon, QuestScreen q) {
        super(p_93367_, p_93368_, 31, 30, p_93369_);
        this.id = id;
        this.icon = icon;
        this.parent = q;
    }

    @Override
    public void render(PoseStack pPoseStack, int pX, int pY, float delta){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0,TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        if(this.parent.currTab == this.id){this.blit(pPoseStack,this.x,this.y,0,30,31,30);}
        else{this.blit(pPoseStack,this.x,this.y,0,0,31,30);}
        this.parent.drawItem(this.x + 7, this.y + 6,this.icon);

    }

    public int getId(){return id;}

    @Override
    public void onPress() {
        this.parent.tabChanged(this.id);
        this.parent.currTab = this.id;
    }

    @Override
    public void updateNarration(NarrationElementOutput p_169152_) {

    }
}
