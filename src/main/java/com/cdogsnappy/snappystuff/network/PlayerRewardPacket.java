package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerRewardPacket {
    private List<ItemStack> playerRewards;
    private int amount;
    private UUID player;
    public PlayerRewardPacket(FriendlyByteBuf buf){
        amount = buf.readInt();
        player = buf.readUUID();
        playerRewards = new ArrayList<>();
        for(int j = 0; j < amount; ++j){
            playerRewards.add(buf.readItem());
        }
    }
    public PlayerRewardPacket(List<ItemStack> playerRewards, UUID player) {
        this.playerRewards = playerRewards;
        this.amount = playerRewards.size();
        this.player = player;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(player);
        List<ItemStack> rewards = QuestHandler.rewardRegistry.get(player);
        for(ItemStack i : rewards){
            buf.writeItem(i);
        }
        buf.writeInt(rewards.size());
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            QuestScreensData.playerRewards = playerRewards;
        });
        return true;
    }

}