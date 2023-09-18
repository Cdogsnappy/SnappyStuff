package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AttemptCompleteQuestPacket {
    private ClosedContractQuest q;
    public AttemptCompleteQuestPacket(FriendlyByteBuf buf){
        q = ClosedContractQuest.load(buf.readNbt());
    }
    public AttemptCompleteQuestPacket(ClosedContractQuest cq){
        q = cq;
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(q.save(new CompoundTag()));
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            int ind = QuestHandler.playerQuestData.get(context.getSender().getUUID()).acceptedQuests.indexOf(q);
            QuestHandler.attemptCompleteQuest(QuestHandler.playerQuestData.get(context.getSender().getUUID()).acceptedQuests.get(ind),context.getSender());
        });
        return true;
    }
}
