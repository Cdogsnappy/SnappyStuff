package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.OpenContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestAcceptPacket {
    ClosedContractQuest q;
    public QuestAcceptPacket(FriendlyByteBuf buf){
        CompoundTag tag = buf.readNbt();
        int questType = tag.getInt("type");
        this.q = ClosedContractQuest.load(tag.getCompound("quest"));
    }


    public QuestAcceptPacket(ClosedContractQuest q) {
        this.q = q;
    }



    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        if(q == null){
            tag.putInt("type",2);
        }
        else{
            tag.put("quest",ClosedContractQuest.save(new CompoundTag(), (ClosedContractQuest) q));
            tag.putInt("type",0);
        }
        buf.writeNbt(tag);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
        });
        return true;
    }
}
