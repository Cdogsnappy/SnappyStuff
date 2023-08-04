package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.commands.CommandRegistration;
import com.cdogsnappy.snappystuff.data.ServerBirth;
import com.cdogsnappy.snappystuff.data.ServerDeath;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import com.cdogsnappy.snappystuff.quest.mission.MissionJSONHandler;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.cdogsnappy.snappystuff.screen.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.io.*;
import java.util.ArrayList;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SnappyStuff.MODID)
public class SnappyStuff
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "snappystuff";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace



    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab

    public SnappyStuff()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        /*
        Just registering event subscribers in THIS CLASS through addListener(), for other classes use the EventBusSubscriber header.
         */
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::imcSend);
        modEventBus.addListener(this::stitch);




        // Register the Deferred Register to the mod event bus so blocks get registered

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        Registration.register(modEventBus);

        // Register the item to a creative tab

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");


    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) throws FileNotFoundException {
        //FMLDedicatedServerSetupEvent may be better for when we are done testing mechanics
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        try {
            ServerBirth.readData();
            RadioHandler.init();
            QuestHandler.get(event.getServer().getLevel(Level.OVERWORLD));
            MissionJSONHandler.init();
            MissionJSONHandler.readDailies();
        }
        catch(Exception e){
            LOGGER.info("FATAL ERROR RELOADING OR INITIALIZING SNAPPY DATA");
        }
    }
    @SubscribeEvent
    public void imcSend(InterModEnqueueEvent event){
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().icon(new ResourceLocation("snappystuff:curios/radio_slot")).build());
    }
    @SubscribeEvent
    public void stitch(TextureStitchEvent.Pre event){
        event.addSprite(new ResourceLocation("snappystuff:curios/radio_slot"));
    }

    @SubscribeEvent
    public void onServerDeath(ServerStoppingEvent event){
        ServerDeath.generateData(LOGGER);
        try {
            //if(event.getServer().isDedicatedServer()) { THIS WILL FIX THE INTEGRATED SERVER ISSUE, KARMALOGS DO NOT WORK ON
            //INTEGRATED SERVERS
                ServerDeath.writeKarmaLogs();
            //}
        }
        catch(IOException e){
            LOGGER.info("FATAL ERROR SAVING KARMA LOGS");
        }
        QuestHandler.setQuestsDirty();
        MissionHandler.dailyMissionList = new ArrayList<>();

    }

    @SubscribeEvent
    public void cmds(RegisterCommandsEvent e){
        CommandRegistration.registerCommands(e);
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
            QuestScreensData.init();
            MenuScreens.register(ModMenus.MUSIC_UPLOAD_MENU.get(), MusicUploadScreen::new);
            MenuScreens.register(ModMenus.QUEST_ACCEPT_MENU.get(), QuestAcceptScreen::new);
            MenuScreens.register(ModMenus.QUEST_CREATE_MENU.get(), QuestCreateScreen::new);
        }
    }


}
