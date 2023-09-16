package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class QuestCancelPacket {

    private ClosedContractQuest cq;
    public QuestCancelPacket(FriendlyByteBuf buf){cq = ClosedContractQuest.load(buf.readNbt());}
    public QuestCancelPacket(ClosedContractQuest cq){this.cq = cq;}
    public void toBytes(FriendlyByteBuf buf){buf.writeNbt(cq.save(new CompoundTag()));}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //HERE WE ARE ON SERVER
            if(cq.questor != null){
                QuestHandler.playerQuestData.get(cq.questor).acceptedQuests.remove(cq);
                SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(cq.questor),context.getSender());
            }
            QuestHandler.playerQuestData.get(cq.requestor).createdQuests.remove(cq);
            ServerPlayer sp = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(cq.requestor);
            if(sp != null){SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(cq.requestor),sp);}
        });
        return true;
    }
}
