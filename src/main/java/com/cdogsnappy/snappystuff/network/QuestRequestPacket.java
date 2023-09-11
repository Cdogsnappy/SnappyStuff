package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.Quest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent when a quest accept GUI needs to render the next available quest for a player. Player is then sent a packet containing the quest
 */
public class QuestRequestPacket {
    private int ind;
    private boolean open;
    public QuestRequestPacket(FriendlyByteBuf buf){
        CompoundTag tag = buf.readNbt();
        this.ind = tag.getInt("ind");
        this.open = tag.getBoolean("open");
    }

    public QuestRequestPacket(int ind,boolean open) {
        this.ind = ind;
        this.open = open;
    }

    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ind",ind);
        tag.putBoolean("open",open);
        buf.writeNbt(tag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Quest q = null;
            if(open){
                int currSize = QuestHandler.openContractQuests.size();
                if(currSize == 0){}
                else if(currSize - 1 < ind){q = QuestHandler.openContractQuests.get(currSize-1);}
                else{q = QuestHandler.openContractQuests.get(ind);}
            }
            else{
                int currSize = QuestHandler.unacceptedQuests.size();
                if(currSize == 0){}
                else if(currSize - 1< ind){q = QuestHandler.unacceptedQuests.get(currSize-1);}
                else{q = QuestHandler.unacceptedQuests.get(ind);}
            }
            // HERE WE ARE ON THE SERVER!
            SnappyNetwork.sendToPlayer(new QuestScreenPacket(q), context.getSender());//SENDS THE NEXT QUEST TO DISPLAY
        });
        return true;
    }

}

