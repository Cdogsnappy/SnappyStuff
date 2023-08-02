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

public class QuestRequestPacket {
    private int ind;
    private boolean open;
    public QuestRequestPacket(FriendlyByteBuf buf){
        this.ind = buf.readInt();
        this.open = buf.readBoolean();
    }


    public QuestRequestPacket(int ind, Boolean type) {
        this.ind = ind;
        this.open = type;
    }



    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ind);
        buf.writeBoolean(open);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Quest q = null;
            if(open){
                if(ind < QuestHandler.openContractQuests.size()){
                    q = QuestHandler.openContractQuests.get(ind);
                }
            }
            else{
                if(ind < QuestHandler.unacceptedQuests.size()) {
                    q = QuestHandler.unacceptedQuests.get(ind);
                }
            }
            // HERE WE ARE ON THE SERVER!
            QuestNetwork.sendToPlayer(new QuestAcceptPacket(q), context.getSender());//SENDS THE NEXT QUEST TO DISPLAY
        });
        return true;
    }

}

