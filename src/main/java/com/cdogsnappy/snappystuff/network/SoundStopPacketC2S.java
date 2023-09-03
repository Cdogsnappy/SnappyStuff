package com.cdogsnappy.snappystuff.network;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import java.util.UUID;
import java.util.function.Supplier;


//This may be unnecessary
public class SoundStopPacketC2S {
    private UUID id;
    private ResourceKey<Level> level;
    private Vec3 vec;

    public SoundStopPacketC2S(FriendlyByteBuf buf) {
        this.id = buf.readUUID();
        this.level = buf.readResourceKey(Registry.DIMENSION_REGISTRY);
        CompoundTag tag = buf.readNbt();
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        this.vec = new Vec3(x,y,z);

    }
    public SoundStopPacketC2S(UUID id, ResourceKey<Level> level, Vec3 vec){
        this.id = id;
        this.level = level;
        this.vec = vec;
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(id);
        buf.writeResourceKey(level);
        CompoundTag tag = new CompoundTag();
        tag.putDouble("x",vec.x);
        tag.putDouble("y",vec.y);
        tag.putDouble("z",vec.z);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            SnappyNetwork.sendToNearbyPlayers(new SoundStopPacketS2C(id),vec,level);
        });
        return true;
    }
}
