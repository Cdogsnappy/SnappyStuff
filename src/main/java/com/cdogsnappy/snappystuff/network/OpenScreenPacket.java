package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.radio.RadioClient;
import com.cdogsnappy.snappystuff.screen.QuestOverviewMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class OpenScreenPacket {

    public OpenScreenPacket(FriendlyByteBuf buf){}
    public OpenScreenPacket(){}
    public void toBytes(FriendlyByteBuf buf){}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            NetworkHooks.openScreen(context.getSender(), new SimpleMenuProvider((w, p, pl) -> new QuestOverviewMenu(w, p, null), Component.literal("Questbook")));
        });
        return true;
    }
}
