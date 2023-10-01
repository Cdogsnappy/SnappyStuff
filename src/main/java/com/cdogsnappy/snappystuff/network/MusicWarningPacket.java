package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.radio.RadioClient;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MusicWarningPacket {
    private UUID id;
    public MusicWarningPacket(FriendlyByteBuf buf){id = buf.readUUID();}
    public MusicWarningPacket(UUID id){this.id = id;}
    public void toBytes(FriendlyByteBuf buf) {buf.writeUUID(id);}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            RadioClient.stopSound(id);
            if(Minecraft.getInstance().player.getUUID().equals(id)) {
                RadioClient.isInStandby = false;
            }
            RadioClient.playStatic(id);
        });
        return true;
    }
}
