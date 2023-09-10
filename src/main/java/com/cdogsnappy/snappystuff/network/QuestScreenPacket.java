package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.OpenContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreen;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sends a packet containing the next quest that a client wants to render
 */
public class QuestScreenPacket {
    Quest q;
    private int unaccepted;
    private int open;
    public QuestScreenPacket(FriendlyByteBuf buf){
        CompoundTag tag = buf.readNbt();
        int questType = tag.getInt("type");
        unaccepted = tag.getInt("unaccepted");
        open = tag.getInt("open");
        this.q = switch(questType){
            case 0 -> ClosedContractQuest.load(tag.getCompound("quest"));
            case 1 -> OpenContractQuest.load(tag.getCompound("quest"));
            case 2 -> null;
            default -> throw new IllegalStateException("Quest packet failure...");
        };

    }


    public QuestScreenPacket(Quest q) {
        this.q = q;
        unaccepted = QuestHandler.unacceptedQuests.size();
        open = QuestHandler.openContractQuests.size();
    }



    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        if(q == null){
            tag.putInt("type",2);
        }
        else if(q instanceof OpenContractQuest){
            tag.put("quest",OpenContractQuest.save(new CompoundTag(),(OpenContractQuest)q));
            tag.putInt("type",1);
        }
        else{
            tag.put("quest",ClosedContractQuest.save(new CompoundTag(), (ClosedContractQuest) q));
            tag.putInt("type",0);
        }
        tag.putInt("unaccepted",unaccepted);
        tag.putInt("open",open);
        buf.writeNbt(tag);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            QuestScreensData.questScreenDisplay = q;
            QuestScreensData.numClosedQuests = unaccepted;
            QuestScreensData.numOpenQuests = open;
        });
        return true;
    }

}
