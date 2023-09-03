package com.cdogsnappy.snappystuff.network;

import java.util.UUID;
import java.util.function.Supplier;
import com.cdogsnappy.snappystuff.radio.RadioClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundStartPacketS2C {
    private UUID id;
    private SoundEvent event;
    private int entityId;
    public SoundStartPacketS2C(FriendlyByteBuf buf) {
        this.id = buf.readUUID();
        this.event = ForgeRegistries.SOUND_EVENTS.getValue(buf.readResourceLocation());
        this.entityId = buf.readInt();
    }
    public SoundStartPacketS2C(UUID id, SoundEvent event, int entityId){
        this.id = id;
        this.event = event;
        this.entityId = entityId;
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(id);
        buf.writeResourceLocation(event.getLocation());
        buf.writeInt(entityId);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            RadioClient.playEntityBoundSound(event,id,entityId);
        });
        return true;
    }

}
