package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.court.ClientCitizenData;
import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AvailablePlayersPacket {
    private List<ClientCitizenData> players;
    public AvailablePlayersPacket(FriendlyByteBuf buf){
        players = new ArrayList<>();
        CompoundTag tag = buf.readNbt();
        ListTag playerTag = (ListTag)tag.get("players");
        for(int i = 0; i<playerTag.size(); ++i){
            players.add(ClientCitizenData.load((playerTag.getCompound(i))));
        }
    }
    public AvailablePlayersPacket(List<ClientCitizenData> players) {
        this.players = players;
    }

    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        ListTag playerTag = new ListTag();
        players.forEach( (s) -> {
            playerTag.add(s.save(new CompoundTag()));
        });
        tag.put("players",playerTag);
        buf.writeNbt(tag);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            CitizenData.citizenNames = players;
            QuestScreensData.playerSearchTokens = players;
        });
        return true;
    }
}
