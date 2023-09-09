package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerRewardRequestPacket {
    public PlayerRewardRequestPacket(FriendlyByteBuf buf){}


    public PlayerRewardRequestPacket() {}



    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //HERE WE ARE ON SERVER
            SnappyNetwork.sendToPlayer(new PlayerRewardPacket(QuestHandler.playerQuestData.get(context.getSender().getUUID()).rewards,context.getSender().getUUID()), context.getSender());
        });
        return true;
    }
}
