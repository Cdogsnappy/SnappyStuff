package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class QuestNetwork {
    private static QuestNetwork instance = null;
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static QuestNetwork getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Attempt to call network getInstance before network is setup");
        }

        return instance;
    }

    public static void setup(){
        if (instance != null) {
            return;
        }

        instance = new QuestNetwork();
        INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(SnappyStuff.MODID, "questnetwork")).simpleChannel();

        INSTANCE.messageBuilder(QuestAcceptPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(QuestAcceptPacket::new)
                .encoder(QuestAcceptPacket::toBytes)
                .consumerMainThread(QuestAcceptPacket::handle)
                .add();
        INSTANCE.messageBuilder(QuestRequestPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(QuestRequestPacket::new)
                .encoder(QuestRequestPacket::toBytes)
                .consumerMainThread(QuestRequestPacket::handle)
                .add();
        INSTANCE.messageBuilder(PlayerQuestPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerQuestPacket::new)
                .encoder(PlayerQuestPacket::toBytes)
                .consumerMainThread(PlayerQuestPacket::handle)
                .add();
        INSTANCE.messageBuilder(PlayerQuestRequestPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerQuestRequestPacket::new)
                .encoder(PlayerQuestRequestPacket::toBytes)
                .consumerMainThread(PlayerQuestRequestPacket::handle)
                .add();
    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
