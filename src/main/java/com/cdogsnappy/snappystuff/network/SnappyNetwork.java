package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;


/**
 * @author Cdogsnappy
 * Packet channel that all quest packets are sent on
 */
public class SnappyNetwork {
    private static SnappyNetwork instance = null;
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static SnappyNetwork getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Attempt to call network getInstance before network is setup");
        }
        return instance;
    }
    public static void setup(){
        if (instance != null) {
            return;
        }

        instance = new SnappyNetwork();
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SnappyStuff.MODID, "questnetwork"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        INSTANCE.messageBuilder(QuestScreenPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(QuestScreenPacket::new)
                .encoder(QuestScreenPacket::toBytes)
                .consumerMainThread(QuestScreenPacket::handle)
                .add();
        INSTANCE.messageBuilder(QuestRequestPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(QuestRequestPacket::new)
                .encoder(QuestRequestPacket::toBytes)
                .consumerMainThread(QuestRequestPacket::handle)
                .add();
        INSTANCE.messageBuilder(PlayerQuestDataPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(PlayerQuestDataPacket::new)
                .encoder(PlayerQuestDataPacket::toBytes)
                .consumerMainThread(PlayerQuestDataPacket::handle)
                .add();
        INSTANCE.messageBuilder(PlayerQuestDataRequestPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(PlayerQuestDataRequestPacket::new)
                .encoder(PlayerQuestDataRequestPacket::toBytes)
                .consumerMainThread(PlayerQuestDataRequestPacket::handle)
                .add();
        INSTANCE.messageBuilder(AvailablePlayersPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(AvailablePlayersPacket::new)
                .encoder(AvailablePlayersPacket::toBytes)
                .consumerMainThread(AvailablePlayersPacket::handle)
                .add();
        INSTANCE.messageBuilder(StandbyResetPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StandbyResetPacket::new)
                .encoder(StandbyResetPacket::toBytes)
                .consumerMainThread(StandbyResetPacket::handle)
                .add();
        INSTANCE.messageBuilder(SoundStopPacketS2C.class,++id,NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SoundStopPacketS2C::new)
                .encoder(SoundStopPacketS2C::toBytes)
                .consumerMainThread(SoundStopPacketS2C::handle)
                .add();
        INSTANCE.messageBuilder(SoundStopPacketC2S.class,++id,NetworkDirection.PLAY_TO_SERVER)
                .decoder(SoundStopPacketC2S::new)
                .encoder(SoundStopPacketC2S::toBytes)
                .consumerMainThread(SoundStopPacketC2S::handle)
                .add();
        INSTANCE.messageBuilder(MusicWarningPacket.class,++id,NetworkDirection.PLAY_TO_CLIENT)
                .decoder(MusicWarningPacket::new)
                .encoder(MusicWarningPacket::toBytes)
                .consumerMainThread(MusicWarningPacket::handle)
                .add();
        INSTANCE.messageBuilder(SoundStartPacketC2S.class,++id,NetworkDirection.PLAY_TO_SERVER)
                .decoder(SoundStartPacketC2S::new)
                .encoder(SoundStartPacketC2S::toBytes)
                .consumerMainThread(SoundStartPacketC2S::handle)
                .add();
        INSTANCE.messageBuilder(SoundStartPacketS2C.class,++id,NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SoundStartPacketS2C::new)
                .encoder(SoundStartPacketS2C::toBytes)
                .consumerMainThread(SoundStartPacketS2C::handle)
                .add();
        INSTANCE.messageBuilder(QuestCreatePacket.class,++id,NetworkDirection.PLAY_TO_SERVER)
                .decoder(QuestCreatePacket::new)
                .encoder(QuestCreatePacket::toBytes)
                .consumerMainThread(QuestCreatePacket::handle)
                .add();
        INSTANCE.messageBuilder(QuestAcceptPacket.class,++id,NetworkDirection.PLAY_TO_SERVER)
                .decoder(QuestAcceptPacket::new)
                .encoder(QuestAcceptPacket::toBytes)
                .consumerMainThread(QuestAcceptPacket::handle)
                .add();
        INSTANCE.messageBuilder(QuestCancelPacket.class, ++id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(QuestCancelPacket::new)
                .encoder(QuestCancelPacket::toBytes)
                .consumerMainThread(QuestCancelPacket::handle)
                .add();
        INSTANCE.messageBuilder(RewardDistributionRequestPacket.class, ++id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(RewardDistributionRequestPacket::new)
                .encoder(RewardDistributionRequestPacket::toBytes)
                .consumerMainThread(RewardDistributionRequestPacket::handle)
                .add();
        INSTANCE.messageBuilder(AttemptCompleteQuestPacket.class, ++id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(AttemptCompleteQuestPacket::new)
                .encoder(AttemptCompleteQuestPacket::toBytes)
                .consumerMainThread(AttemptCompleteQuestPacket::handle)
                .add();
    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);}
    public static <MSG> void sendToAllPlayers(MSG message){
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
    public static <MSG> void sendToNearbyPlayers(MSG message, Vec3 vec, ResourceKey<Level> level) {
        INSTANCE.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(vec.x,vec.y,vec.z,128,level)),message);
    }
}
