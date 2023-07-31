package com.cdogsnappy.snappystuff.sounds;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.radio.CustomSoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class SSSoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SnappyStuff.MODID);

    public static final RegistryObject<SoundEvent> DREAM_SWEET = createSoundEvent("dream_sweet", true);
    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName, boolean uploadable) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(SnappyStuff.MODID,soundName));
        return SOUNDS.register(soundName, () -> sound);
    }
}
