package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.QuestData;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;


/**
 * Packet sent by the server whenever it is told that a client needs to refresh it's player quests.
 */
public class PlayerQuestDataPacket {
    private QuestData qd;
    public PlayerQuestDataPacket(FriendlyByteBuf buf){qd = QuestData.load(buf.readNbt());}
    public PlayerQuestDataPacket(UUID player) {qd = QuestHandler.playerQuestData.get(player);}
    public void toBytes(FriendlyByteBuf buf) {buf.writeNbt(qd.save(new CompoundTag()));}
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            QuestScreensData.questData = qd;
        });
        return true;
    }
}
