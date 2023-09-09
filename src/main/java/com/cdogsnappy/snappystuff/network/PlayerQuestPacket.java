package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;


/**
 * Packet sent by the server whenever it is told that a client needs to refresh it's player quests.
 */
public class PlayerQuestPacket {
    private List<ClosedContractQuest> playerQuests;
    private UUID player;
    public PlayerQuestPacket(FriendlyByteBuf buf){
        CompoundTag tag = buf.readNbt();
        player = tag.getUUID("player");
        playerQuests = new ArrayList<>();
        ListTag quests = (ListTag)tag.get("quests");
        for(int j = 0; j < quests.size(); ++j){
            playerQuests.add(ClosedContractQuest.load(quests.getCompound(j)));
        }
    }
    public PlayerQuestPacket(List<ClosedContractQuest> playerQuests, UUID player) {
        this.playerQuests = playerQuests;
        this.player = player;
    }

    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("player",player);
        List<ClosedContractQuest> quests = QuestHandler.playerQuestData.get(player).acceptedQuests;
        ListTag questTag = new ListTag();
        quests.forEach((q) -> {
            questTag.add(ClosedContractQuest.save(new CompoundTag(),q));
        });
        tag.put("quests",questTag);
        buf.writeNbt(tag);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            QuestScreensData.playerQuests = playerQuests;
        });
        return true;
    }
}
