package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.commands.EndorseCommand;
import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.karma.KarmaPlayerInfo;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.cdogsnappy.snappystuff.screen.ModMenus;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import com.cdogsnappy.snappystuff.screen.MusicUploadScreen;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

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

        // Register the commonSetup method for modloading
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
    public void onServerStarting(ServerStartingEvent event){
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        File karma = new File("karma.txt");
        try {
            if (karma.exists()) {
                FileInputStream fos = new FileInputStream(karma);
                ObjectInputStream objReader = new ObjectInputStream(fos);
                Karma.karmaScores = (HashMap<UUID, KarmaPlayerInfo>) objReader.readObject();
                objReader.close();
                LOGGER.info("SUCCESSFULLY RESTORED SNAPPYSTUFF DATA");
            }
            else{
                Karma.init();
            }
        }
        catch(Exception e){
            LOGGER.info("FATAL ERROR RELOADING DATA");
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
        try {
            File karma = new File("karma.txt");
            if (karma.exists()) {
                karma.delete();
            }
            karma.createNewFile();
            FileOutputStream fos = new FileOutputStream(karma);
            ObjectOutputStream objWriter = new ObjectOutputStream(fos);
            HashMap<UUID, KarmaPlayerInfo> toSave = Karma.karmaScores;
            objWriter.writeObject(toSave);
            objWriter.flush();
            objWriter.close();
            LOGGER.info("SUCCESS WRITING DATA");
        }
        catch(IOException e){
            LOGGER.info("FATAL ERROR WRITING DATA");
        }
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
            MenuScreens.register(ModMenus.MUSIC_UPLOAD_MENU.get(), MusicUploadScreen::new);
        }
    }


}
