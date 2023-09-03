package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.radio.RadioClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class SoundStartPacketC2S {
    private UUID id;
    private SoundEvent event;
    private ResourceKey<Level> level;
    public SoundStartPacketC2S(FriendlyByteBuf buf) {
        this.id = buf.readUUID();
        this.event = ForgeRegistries.SOUND_EVENTS.getValue(buf.readResourceLocation());
        this.level = buf.readResourceKey(Registry.DIMENSION_REGISTRY);
    }
    public SoundStartPacketC2S(UUID id, SoundEvent event, ResourceKey<Level> level){
        this.id = id;
        this.event = event;
        this.level = level;
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(id);
        buf.writeResourceLocation(event.getLocation());
        buf.writeResourceKey(level);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            Entity e = ServerLifecycleHooks.getCurrentServer().getLevel(level).getEntity(id);
            SnappyNetwork.sendToNearbyPlayers(new SoundStartPacketS2C(id,event,e.getId()), e.position(),level);
        });
        return true;
    }
}
