package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SnappyStuff.MODID);

    public static final RegistryObject<RecordItem> DREAM_SWEET = makeRecord("dream_sweet", SSSoundRegistry.DREAM_SWEET,420);
    public static final RegistryObject<Item> RADIO = ITEMS.register("radio", () -> new Item(new Item.Properties().stacksTo(1)));



    public static RegistryObject<RecordItem> makeRecord(String name, RegistryObject<SoundEvent> sound, int seconds){
         return ITEMS.register(name, () ->
                new RecordItem(0,sound,new Item.Properties().rarity(Rarity.EPIC).fireResistant(), (seconds+2)*20));

    }
}
