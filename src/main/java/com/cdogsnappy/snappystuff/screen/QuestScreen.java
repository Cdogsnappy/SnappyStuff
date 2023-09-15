package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.quest.mission.*;
import com.cdogsnappy.snappystuff.util.ClientEntityCache;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class QuestScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected int currTab = 0;
    protected int spin = 65;
    public QuestScreen(AbstractContainerMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super((T) p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {

    }
    protected void renderMission(PoseStack pPoseStack, int x, int y, float width, float height, Mission m){
        if(m == null){return;}
        String msg = "";
        switch(m.missionType){
            case COLLECT:
                CollectMission cm = (CollectMission)m;
                msg = msg + "Collect " + (cm.numToCollect-cm.numCollected);
                drawItem(x+8 + this.font.width(msg), y - 26,cm.toCollect);
                break;
            case BLOCK:
                BlockMission bm = (BlockMission)m;
                msg = msg + "Break " + bm.numToBreak;
                drawItem(x+8 + this.font.width(msg), y - 26,new ItemStack(bm.toBreak.asItem()));
                break;
            case KILL:
                KillMission km = (KillMission)m;
                msg = msg + "Kill " + (km.numToKill-km.numKills);
                renderEntityInInventory(x + 18 + this.font.width(msg), y, 10,0,0,ClientEntityCache.computeIfAbsent(km.toKill,Minecraft.getInstance().level));
                break;
            case KILL_PLAYER:
                PlayerKillMission pkm = (PlayerKillMission)m;
                msg = msg + "Kill " + pkm.player;
                break;
        }
        this.font.draw(pPoseStack, msg, x+4,y-16,0);
    }
    public void drawItem(int x, int y, ItemStack is) {
        this.itemRenderer.blitOffset = 100.0F;
        this.itemRenderer.renderAndDecorateItem(is, x, y);
        this.itemRenderer.blitOffset = 0.0F;
    }

    /**
     * CREDIT: Shadows-of-Fire
     * https://github.com/Shadows-of-Fire/Hostile-Neural-Networks/blob/1.19/src/main/java/shadows/hostilenetworks
     */
    public void renderEntityInInventory(float pPosX, float pPosY, float pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        EntityDimensions eD = pLivingEntity.getType().getDimensions();
        pLivingEntity.yBodyRot = this.spin % 360;
        float pScaleScale = 1;
        if(eD.height > 1.95F && eD.width > 1.3964844F){pScaleScale = Math.min(1.95F/eD.height,1.3964844F/eD.width);}
        else if(eD.height > 1.95F){pScaleScale = 1.95F/eD.height;}
        else if(eD.width > 1.3964844F){pScaleScale = 1.3964844F/eD.width;}
        pScale*=pScaleScale;
        float f1 = (float) Math.atan(pMouseY / 40.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(pPosX, pPosY, 1050.0F);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack matrixstack = new PoseStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale(pScale, pScale, pScale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        matrixstack.mulPose(quaternion);
        matrixstack.mulPose(Vector3f.YP.rotationDegrees((this.spin + this.minecraft.getDeltaFrameTime()) * 2.25F % 360));
        pLivingEntity.setYRot(0);
        pLivingEntity.yBodyRot = pLivingEntity.getYRot();
        pLivingEntity.yHeadRot = pLivingEntity.getYRot();
        pLivingEntity.yHeadRotO = pLivingEntity.getYRot();
        EntityRenderDispatcher entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        MultiBufferSource.BufferSource rtBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.render(pLivingEntity, 0, 0, 0, 0.0F, 1, matrixstack, rtBuffer, 15728880);
        });
        rtBuffer.endBatch();
        entityrenderermanager.setRenderShadow(true);
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }
    @Override
    public void containerTick(){
        spin++;
    }

    public void tabChanged(int id){}

}
