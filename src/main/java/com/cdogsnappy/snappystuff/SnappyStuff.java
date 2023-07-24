package com.cdogsnappy.snappystuff;

import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

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
    public void onServerStarting(ServerStartingEvent event)
    {

        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    @SubscribeEvent
    public void imcSend(InterModEnqueueEvent event){
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
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
    @SubscribeEvent
    public void ServerSideSetup(FMLDedicatedServerSetupEvent event){
        MinecraftForge.EVENT_BUS.register(new RadioHandler());

    }


}
