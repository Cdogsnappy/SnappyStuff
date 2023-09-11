package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.OpenContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestAcceptPacket {
    ClosedContractQuest q;
    public QuestAcceptPacket(FriendlyByteBuf buf){
        this.q = ClosedContractQuest.load(buf.readNbt());
    }
    public QuestAcceptPacket(ClosedContractQuest q) {
        this.q = q;
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(ClosedContractQuest.save(new CompoundTag(),q));
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            QuestHandler.acceptQuest(q,context.getSender());
        });
        return true;
    }
}
