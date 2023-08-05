package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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
    private int amount;
    private UUID player;
    public PlayerQuestPacket(FriendlyByteBuf buf){
        amount = buf.readInt();
        player = buf.readUUID();
        playerQuests = new ArrayList<>();
        for(int j = 0; j < amount; ++j){
            playerQuests.add(ClosedContractQuest.load(buf.readNbt(), true));
        }
    }
    public PlayerQuestPacket(List<ClosedContractQuest> playerQuests, UUID player) {
        this.playerQuests = playerQuests;
        this.amount = playerQuests.size();
        this.player = player;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(player);
        List<ClosedContractQuest> quests = QuestHandler.acceptedQuests.get(player);
        for(ClosedContractQuest q : quests){
            buf.writeNbt(ClosedContractQuest.save(new CompoundTag(), q));
        }
        buf.writeInt(quests.size());
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
