package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerRewardPacket {
    private List<ItemStack> playerRewards;
    private UUID player;
    public PlayerRewardPacket(FriendlyByteBuf buf){
        CompoundTag tag = buf.readNbt();
        ListTag rewards = (ListTag)tag.get("rewards");
        playerRewards = new ArrayList<>();
        for(int j = 0; j < rewards.size(); ++j){
            playerRewards.add(ItemStack.of(rewards.getCompound(j)));
        }
    }
    public PlayerRewardPacket(List<ItemStack> playerRewards, UUID player) {
        this.playerRewards = playerRewards;
        this.player = player;
    }
    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        ListTag rewardList = new ListTag();
        List<ItemStack> rewards = QuestHandler.rewardRegistry.get(player);
        rewards.forEach((i) -> {
            rewardList.add(i.save(new CompoundTag()));
        });
        tag.put("rewards",rewardList);
        buf.writeNbt(tag);
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