package com.cdogsnappy.snappystuff.sounds;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.radio.CustomSoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SSSoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SnappyStuff.MODID);

    public static final RegistryObject<SoundEvent> DREAM_SWEET = createSoundEvent("dream_sweet", 420);
    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName, int seconds) {
        return SOUNDS.register(soundName, () -> new CustomSoundEvent(new ResourceLocation(SnappyStuff.MODID, soundName),(seconds+2)*20));
    }
}
