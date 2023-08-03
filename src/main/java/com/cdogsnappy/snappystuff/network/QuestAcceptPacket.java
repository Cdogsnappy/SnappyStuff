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

/**
 * Sends a packet containing the next quest that a client wants to render
 */
public class QuestAcceptPacket {
    Quest q;
    public QuestAcceptPacket(FriendlyByteBuf buf){
        int questType = buf.readInt();
        this.q = switch(questType){
            case 0 -> ClosedContractQuest.load(buf.readNbt(),false);
            case 1 -> OpenContractQuest.load(buf.readNbt());
            case 2 -> null;
            default -> throw new IllegalStateException("Quest packet failure...");
        };

    }


    public QuestAcceptPacket(Quest q) {
        this.q = q;
    }



    public void toBytes(FriendlyByteBuf buf) {
        if(q == null){
            buf.writeInt(2);
        }
        if(q instanceof OpenContractQuest){
            buf.writeNbt(OpenContractQuest.save(new CompoundTag(),(OpenContractQuest)q));
            buf.writeInt(1);
        }
        else{
            buf.writeNbt(ClosedContractQuest.save(new CompoundTag(), (ClosedContractQuest) q));
            buf.writeInt(0);
        }

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            QuestScreensData.questAcceptScreenDisplay = q;
        });
        return true;
    }

}
