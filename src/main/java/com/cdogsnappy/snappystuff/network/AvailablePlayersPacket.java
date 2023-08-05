package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.screen.QuestScreensData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AvailablePlayersPacket {
    private List<String> players;
    public AvailablePlayersPacket(FriendlyByteBuf buf){
        players = new ArrayList<>();
        CompoundTag tag = buf.readNbt();
        ListTag playerTag = (ListTag)tag.get("players");
        for(int i = 0; i<playerTag.size(); ++i){
            players.add((playerTag.getCompound(i).getString("player")));
        }
    }
    public AvailablePlayersPacket(List<String> players) {
        this.players = players;
    }

    public void toBytes(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        ListTag playerTag = new ListTag();
        players.forEach( (s) -> {
            CompoundTag player = new CompoundTag();
            player.putString("player",s);
            playerTag.add(player);
        });
        tag.put("players",playerTag);
        buf.writeNbt(tag);
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
