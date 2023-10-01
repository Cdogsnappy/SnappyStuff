package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.radio.RadioClient;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UploadPacket {
    private ItemStack i;
    public UploadPacket(FriendlyByteBuf buf){
        i = ItemStack.of(buf.readNbt());
    }
    public UploadPacket(ItemStack item){
        i = item;
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeNbt(i.save(new CompoundTag()));
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            RadioHandler.uploadSong((RecordItem)i.getItem());
        });
        return true;
    }
}
