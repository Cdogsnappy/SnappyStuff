package com.cdogsnappy.snappystuff.network;

import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class QuestNetwork {
    private static QuestNetwork instance = null;
    public static SimpleChannel network;
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
        network = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(SnappyStuff.MODID, "questnetwork")).simpleChannel();
    }
}
