package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.armor.DivineArmorItem;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SnappyStuff.MODID);

    public static final RegistryObject<RecordItem> DREAM_SWEET = makeRecord("dream_sweet", SSSoundRegistry.DREAM_SWEET,420);
    public static final RegistryObject<Item> RADIO = ITEMS.register("radio", () -> new RadioItem(new Item.Properties().stacksTo(1).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));
    public static final RegistryObject<Item> DIVINE_FRUIT = ITEMS.register("divine_fruit", () -> new DivineFruitItem(new Item.Properties().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)
            .rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(0).alwaysEat().build())));
    /*
    public static final RegistryObject<Item> DIVINE_ARMOR_HELMET = ITEMS.register("divine_armor_helmet", () -> new DivineArmorItem(ArmorMaterials.NETHERITE,
            EquipmentSlot.HEAD, new ArmorItem.Properties()));
    public static final RegistryObject<Item> DIVINE_ARMOR_CHEST = ITEMS.register("divine_armor_chest", () -> new DivineArmorItem(ArmorMaterials.NETHERITE,
            EquipmentSlot.CHEST, new ArmorItem.Properties()));
    public static final RegistryObject<Item> DIVINE_ARMOR_LEGS = ITEMS.register("divine_armor_legs", () -> new DivineArmorItem(ArmorMaterials.NETHERITE,
            EquipmentSlot.LEGS, new ArmorItem.Properties()));
    public static final RegistryObject<Item> DIVINE_ARMOR_FEET = ITEMS.register("divine_armor_feet", () -> new DivineArmorItem(ArmorMaterials.NETHERITE,
            EquipmentSlot.FEET, new ArmorItem.Properties()));
            */
    public static final RegistryObject<Item> DIVINIUM_INGOT = ITEMS.register("divinium_ingot", () -> new Item(new Item.Properties().rarity(ModRarities.DIVINE).fireResistant().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));
    public static final RegistryObject<Item> DIVINIUM_nugget = ITEMS.register("divinium_nugget", () -> new Item(new Item.Properties().rarity(ModRarities.DIVINE).fireResistant().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));

    public static final RegistryObject<Item> DEMONIC_INGOT = ITEMS.register("demonic_ingot", () -> new Item(new Item.Properties().rarity(ModRarities.DEMONIC).fireResistant().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));
    public static final RegistryObject<Item> DEMONIC_NUGGET = ITEMS.register("demonic_nugget", () -> new Item(new Item.Properties().rarity(ModRarities.DEMONIC).fireResistant().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));
    public static final RegistryObject<Item> NEUTRALIUM_INGOT = ITEMS.register("neutralium_ingot", () -> new Item(new Item.Properties().rarity(ModRarities.LEGENDARY).fireResistant().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));
    public static final RegistryObject<Item> NEUTRALIUM_NUGGET = ITEMS.register("neutralium_nugget", () -> new Item(new Item.Properties().rarity(ModRarities.LEGENDARY).fireResistant().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));
    public static final RegistryObject<Item> DEMON_BLADE = ITEMS.register("demon_blade", () -> new DemonBladeItem(ModTiers.DEMON,2,3f, new Item.Properties().rarity(ModRarities.DEMONIC).fireResistant().tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));


    public static RegistryObject<RecordItem> makeRecord(String name, RegistryObject<SoundEvent> sound, int seconds){
         return ITEMS.register(name, () ->
                new RecordItem(0,sound,new Item.Properties().rarity(Rarity.EPIC).fireResistant().tab(SnappyStuffTabs.SNAPPY_MUSIC_TAB),(seconds+2)*20));

    }
}
