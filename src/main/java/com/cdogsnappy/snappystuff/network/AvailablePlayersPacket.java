package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.court.CitizenData;
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

public class AvailablePlayersPacket {
    private List<String> players;
    private int amount;
    public AvailablePlayersPacket(FriendlyByteBuf buf){
        amount = buf.readInt();
        players = new ArrayList<>();
        for(int j = 0; j < amount; ++j){
            players.add(buf.readUtf());
        }
    }
    public AvailablePlayersPacket(List<String> players) {
        this.players = players;
        this.amount = players.size();
    }

    public void toBytes(FriendlyByteBuf buf) {
        for(String s : players){
           buf.writeUtf(s);
        }
        buf.writeInt(players.size());
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            QuestScreensData.playerSearchTokens = players;
        });
        return true;
    }
}
