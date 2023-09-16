package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.blocks.QuestCreateBlock;
import com.cdogsnappy.snappystuff.blocks.entity.QuestCreationBlockEntity;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.OpenContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.function.Supplier;

public class QuestCreatePacket {
    ClosedContractQuest cq;
    OpenContractQuest oq;
    int type;
    BlockPos pos;

    public QuestCreatePacket(FriendlyByteBuf buf){
        CompoundTag tag = buf.readNbt();
        int questType = tag.getInt("type");
        CompoundTag quest = tag.getCompound("quest");
        switch(questType){
            case 1 -> oq = OpenContractQuest.load(quest);
            case 0 -> cq = ClosedContractQuest.load(quest);
        }
        type = questType;
        pos = new BlockPos(tag.getInt("x"),tag.getInt("y"),tag.getInt("z"));

    }


    public QuestCreatePacket(OpenContractQuest q, BlockPos pos) {
        this.oq = q;
        this.type = 1;
        this.pos = pos;
    }
    public QuestCreatePacket(ClosedContractQuest q, BlockPos pos) {
        this.cq = q;
        this.type = 0;
        this.pos = pos;
    }



    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        if(type == 0){
            tag.put("quest",cq.save(new CompoundTag()));
            tag.putInt("type",0);
        }
        else{
            tag.put("quest",oq.save(new CompoundTag()));
            tag.putInt("type",1);
        }
        tag.putInt("x",pos.getX());
        tag.putInt("y",pos.getY());
        tag.putInt("z",pos.getZ());
        buf.writeNbt(tag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            if(type == 0){
                QuestHandler.unacceptedQuests.add(cq);
                QuestHandler.playerQuestData.get(cq.requestor).createdQuests.add(cq);
            }
            else{
                QuestHandler.openContractQuests.add(oq);
                QuestHandler.playerQuestData.get(cq.requestor).createdQuests.add(cq);
            }
        });
        QuestCreationBlockEntity be = (QuestCreationBlockEntity)context.getSender().level.getBlockEntity(pos);
        be.clearRewardSlots();
        return true;
    }
}
