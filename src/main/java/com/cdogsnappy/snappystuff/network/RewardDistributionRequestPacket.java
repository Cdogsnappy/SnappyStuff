package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RewardDistributionRequestPacket {
    public RewardDistributionRequestPacket(FriendlyByteBuf buf){}
    public RewardDistributionRequestPacket(){}
    public void toBytes(FriendlyByteBuf buf){}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            QuestHandler.playerQuestData.get(context.getSender().getUUID()).disperseRewards(context.getSender());
        });
        return true;
    }
}
