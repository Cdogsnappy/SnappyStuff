package com.cdogsnappy.snappymod;

import com.cdogsnappy.snappymod.commands.EndorseCommand;
import com.cdogsnappy.snappymod.karma.Karma;
import com.cdogsnappy.snappymod.karma.KarmaPlayerInfo;
import com.cdogsnappy.snappymod.karma.SmiteHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SnappyMod.MODID)
public class SnappyMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "snappymod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final Karma k = new Karma();

    public SnappyMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        // Register the Deferred Register to the mod event bus so items get registered
        // Register the Deferred Register to the mod event bus so tabs get registered

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(k);
        MinecraftForge.EVENT_BUS.register(new TickingHandler());

        // Register the item to a creative tab

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");


    }

    // Add the example block item to the building blocks tab


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) throws IOException, ClassNotFoundException {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        File karma = new File("karma.txt");
        if(karma.exists()){
            FileInputStream fos = new FileInputStream(karma);
            ObjectInputStream objReader = new ObjectInputStream(fos);
            k.karmaScores = (HashMap<UUID, KarmaPlayerInfo>) objReader.readObject();
            objReader.close();
        }
    }
    @SubscribeEvent
    public void onServerDeath(ServerStoppingEvent event) throws IOException {
        File karma = new File("karma.txt");
        if(karma.exists()){karma.delete();}
        karma.createNewFile();
        FileOutputStream fos = new FileOutputStream(karma);
        ObjectOutputStream objWriter = new ObjectOutputStream(fos);
        HashMap<UUID, KarmaPlayerInfo> toSave = k.karmaScores;
        objWriter.writeObject(toSave);
        objWriter.flush();
        objWriter.close();
    }

    @SubscribeEvent
    public void cmds(RegisterCommandsEvent e){
        var builder = Commands.literal("snappy");
        EndorseCommand.register(builder);
        e.getDispatcher().register(builder);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
