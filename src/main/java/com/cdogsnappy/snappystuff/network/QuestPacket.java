package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.OpenContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class QuestPacket {
    Quest q;
    public QuestPacket(FriendlyByteBuf buf){
        encode(buf);
    }
    public void encode(FriendlyByteBuf packetBuffer) {
        if(q instanceof ClosedContractQuest){
            packetBuffer.writeNbt(ClosedContractQuest.save(new CompoundTag(), (ClosedContractQuest)q));
            packetBuffer.writeInt(0);
        }
        else{
            packetBuffer.writeNbt(OpenContractQuest.save(new CompoundTag(), (OpenContractQuest)q));
            packetBuffer.writeInt(1);
        }
    }
    public void decode(FriendlyByteBuf packetBuffer){

    }

    public static void handle(QuestPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientQuestPacketHandler.handlePacket(msg, ctx))
        );
        ctx.get().setPacketHandled(true);
    }

}
