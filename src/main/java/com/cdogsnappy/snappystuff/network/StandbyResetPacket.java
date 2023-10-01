package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.radio.RadioClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StandbyResetPacket {
    private boolean side;
    public StandbyResetPacket(FriendlyByteBuf buf){
        side = buf.readBoolean();
    }
    public StandbyResetPacket(boolean side){
        this.side = side;
    }
    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(side);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            RadioClient.isInStandby = side;
        });
        return true;
    }
}
