package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Empty marker packet, basically just tells the server that a client needs to refresh the player's accepted quests on their end.
 */
public class PlayerQuestDataRequestPacket {
    public PlayerQuestDataRequestPacket(FriendlyByteBuf buf){}


    public PlayerQuestDataRequestPacket() {}



    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //HERE WE ARE ON SERVER
            SnappyNetwork.sendToPlayer(new PlayerQuestDataPacket(context.getSender().getUUID()), context.getSender());
        });
        return true;
    }
}
