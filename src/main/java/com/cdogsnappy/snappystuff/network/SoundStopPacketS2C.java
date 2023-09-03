package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.radio.RadioClient;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SoundStopPacketS2C {
    private UUID id;
    public SoundStopPacketS2C(FriendlyByteBuf buf) {
        this.id = buf.readUUID();
    }
    public SoundStopPacketS2C(UUID id){
        this.id = id;
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(id);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            RadioClient.stopSound(id);
        });
        return true;
    }
}
