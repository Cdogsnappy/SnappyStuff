package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.armor.DivineArmorItem;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SnappyStuff.MODID);

    public static final RegistryObject<RecordItem> AFRICA = makeRecord("africa", SSSoundRegistry.AFRICA, 291);
    public static final RegistryObject<RecordItem> BLACKBIRD = makeRecord("blackbird", SSSoundRegistry.BLACKBIRD, 138);
    public static final RegistryObject<RecordItem> BRANDY_YOURE_A_FINE_GIRL = makeRecord("brandy_youre_a_fine_girl", SSSoundRegistry.BRANDY_YOURE_A_FINE_GIRL, 188);
    public static final RegistryObject<RecordItem> BROWN_EYED_GIRL = makeRecord("brown_eyed_girl", SSSoundRegistry.BROWN_EYED_GIRL, 184);
    public static final RegistryObject<RecordItem> CARRY_ON_MY_WAYWARD_SON = makeRecord("carry_on_my_wayward_son", SSSoundRegistry.CARRY_ON_MY_WAYWARD_SON, 323);
    public static final RegistryObject<RecordItem> DANCING_IN_THE_DARK = makeRecord("dancing_in_the_dark", SSSoundRegistry.DANCING_IN_THE_DARK, 234);
    public static final RegistryObject<RecordItem> DANCING_IN_THE_MOONLIGHT = makeRecord("dancing_in_the_moonlight", SSSoundRegistry.DANCING_IN_THE_MOONLIGHT, 181);
    public static final RegistryObject<RecordItem> DONT_FEAR_THE_REAPER = makeRecord("dont_fear_the_reaper", SSSoundRegistry.DONT_FEAR_THE_REAPER, 308);
    public static final RegistryObject<RecordItem> DREAM_SWEET_IN_SEA_MAJOR = makeRecord("dream_sweet_in_sea_major", SSSoundRegistry.DREAM_SWEET_IN_SEA_MAJOR, 420);
    public static final RegistryObject<RecordItem> DUST_IN_THE_WIND = makeRecord("dust_in_the_wind", SSSoundRegistry.DUST_IN_THE_WIND, 205);
    public static final RegistryObject<RecordItem> EAGLE = makeRecord("eagle", SSSoundRegistry.EAGLE, 200);
    public static final RegistryObject<RecordItem> FEELS_LIKE_THE_FIRST_TIME = makeRecord("feels_like_the_first_time", SSSoundRegistry.FEELS_LIKE_THE_FIRST_TIME, 232);
    public static final RegistryObject<RecordItem> FREE_FALLIN = makeRecord("free_fallin", SSSoundRegistry.FREE_FALLIN, 260);
    public static final RegistryObject<RecordItem> HAVE_YOU_EVER_SEEN_THE_RAIN = makeRecord("have_you_ever_seen_the_rain", SSSoundRegistry.HAVE_YOU_EVER_SEEN_THE_RAIN, 165);
    public static final RegistryObject<RecordItem> HERE_I_GO_AGAIN = makeRecord("here_i_go_again", SSSoundRegistry.HERE_I_GO_AGAIN, 275);
    public static final RegistryObject<RecordItem> HOT_BLOODED = makeRecord("hot_blooded", SSSoundRegistry.HOT_BLOODED, 267);
    public static final RegistryObject<RecordItem> HOTEL_CALIFORNIA = makeRecord("hotel_california", SSSoundRegistry.HOTEL_CALIFORNIA, 391);
    public static final RegistryObject<RecordItem> HOUSE_OF_THE_RISING_SUN = makeRecord("house_of_the_rising_sun", SSSoundRegistry.HOUSE_OF_THE_RISING_SUN, 269);
    public static final RegistryObject<RecordItem> HUNGRY_LIKE_THE_WOLF = makeRecord("hungry_like_the_wolf", SSSoundRegistry.HUNGRY_LIKE_THE_WOLF, 221);
    public static final RegistryObject<RecordItem> I_WANT_TO_KNOW_WHAT_LOVE_IS = makeRecord("i_want_to_know_what_love_is", SSSoundRegistry.I_WANT_TO_KNOW_WHAT_LOVE_IS, 303);
    public static final RegistryObject<RecordItem> LISTEN_TO_THE_MUSIC = makeRecord("listen_to_the_music", SSSoundRegistry.LISTEN_TO_THE_MUSIC, 227);
    public static final RegistryObject<RecordItem> LONELY_IS_THE_NIGHT = makeRecord("lonely_is_the_night", SSSoundRegistry.LONELY_IS_THE_NIGHT, 282);
    public static final RegistryObject<RecordItem> MANEATER = makeRecord("maneater", SSSoundRegistry.MANEATER, 265);
    public static final RegistryObject<RecordItem> MARGARITAVILLE = makeRecord("margaritaville", SSSoundRegistry.MARGARITAVILLE, 248);
    public static final RegistryObject<RecordItem> MAYBE_IM_AMAZED = makeRecord("maybe_im_amazed", SSSoundRegistry.MAYBE_IM_AMAZED, 231);
    public static final RegistryObject<RecordItem> OLD_TIME_ROCK_AND_ROLL = makeRecord("old_time_rock_and_roll", SSSoundRegistry.OLD_TIME_ROCK_AND_ROLL, 194);
    public static final RegistryObject<RecordItem> PRIVATE_EYES = makeRecord("private_eyes", SSSoundRegistry.PRIVATE_EYES, 213);
    public static final RegistryObject<RecordItem> RAMBLIN_MAN = makeRecord("ramblin_man", SSSoundRegistry.RAMBLIN_MAN, 287);
    public static final RegistryObject<RecordItem> RESONANCE = makeRecord("resonance", SSSoundRegistry.RESONANCE, 212);
    public static final RegistryObject<RecordItem> RICH_GIRL = makeRecord("rich_girl", SSSoundRegistry.RICH_GIRL, 144);
    public static final RegistryObject<RecordItem> ROCK_YOU_LIKE_A_HURRICANE = makeRecord("rock_you_like_a_hurricane", SSSoundRegistry.ROCK_YOU_LIKE_A_HURRICANE, 256);
    public static final RegistryObject<RecordItem> SICKO_MODE = makeRecord("sicko_mode", SSSoundRegistry.SICKO_MODE, 323);
    public static final RegistryObject<RecordItem> STAND_BY_ME = makeRecord("stand_by_me", SSSoundRegistry.STAND_BY_ME, 111);
    public static final RegistryObject<RecordItem> STUCK_IN_THE_MIDDLE_WITH_YOU = makeRecord("stuck_in_the_middle_with_you", SSSoundRegistry.STUCK_IN_THE_MIDDLE_WITH_YOU, 108);
    public static final RegistryObject<RecordItem> SWEET_HOME_ALABAMA = makeRecord("sweet_home_alabama", SSSoundRegistry.SWEET_HOME_ALABAMA, 283);
    public static final RegistryObject<RecordItem> TAKE_ME_HOME_TONIGHT = makeRecord("take_me_home_tonight", SSSoundRegistry.TAKE_ME_HOME_TONIGHT, 214);
    public static final RegistryObject<RecordItem> THE_CHAIN = makeRecord("the_chain", SSSoundRegistry.THE_CHAIN, 271);
    public static final RegistryObject<RecordItem> THE_JOKER = makeRecord("the_joker", SSSoundRegistry.THE_JOKER, 217);
    public static final RegistryObject<RecordItem> UNION_DIXIE = makeRecord("union_dixie", SSSoundRegistry.UNION_DIXIE, 142);
    public static final RegistryObject<RecordItem> WE_BUILT_THIS_CITY = makeRecord("we_built_this_city", SSSoundRegistry.WE_BUILT_THIS_CITY, 291);
    public static final RegistryObject<RecordItem> WERE_FINALLY_LANDING = makeRecord("were_finally_landing", SSSoundRegistry.WERE_FINALLY_LANDING, 272);
    public static final RegistryObject<RecordItem> WHEEL_IN_THE_SKY = makeRecord("wheel_in_the_sky", SSSoundRegistry.WHEEL_IN_THE_SKY, 252);
    public static final RegistryObject<RecordItem> WOMAN = makeRecord("woman", SSSoundRegistry.WOMAN, 215);
    public static final RegistryObject<RecordItem> YOU_REALLY_GOT_ME = makeRecord("you_really_got_me", SSSoundRegistry.YOU_REALLY_GOT_ME, 134);
    public static final RegistryObject<RecordItem> YOUR_SONG = makeRecord("your_song", SSSoundRegistry.YOUR_SONG, 243);
    public static final RegistryObject<RecordItem> METAMODERNITY = makeRecord("metamodernity", SSSoundRegistry.METAMODERNITY, 162);
    public static final RegistryObject<RecordItem> AQUARIUM = makeRecord("aquarium", SSSoundRegistry.AQUARIUM, 170);
    public static final RegistryObject<RecordItem> EYE_OF_THE_TIGER = makeRecord("eye_of_the_tiger", SSSoundRegistry.EYE_OF_THE_TIGER, 243);
    public static final RegistryObject<RecordItem> GODZILLA = makeRecord("godzilla", SSSoundRegistry.GODZILLA, 221);
    public static final RegistryObject<RecordItem> HEART_ATTACK = makeRecord("heart_attack", SSSoundRegistry.HEART_ATTACK, 198);
    public static final RegistryObject<RecordItem> HOT_STUFF = makeRecord("hot_stuff", SSSoundRegistry.HOT_STUFF, 228);
    public static final RegistryObject<RecordItem> LET_IT_PLEASE_BE_YOU = makeRecord("let_it_please_be_you", SSSoundRegistry.LET_IT_PLEASE_BE_YOU, 138);
    public static final RegistryObject<RecordItem> MY_HEART_IS_SAD = makeRecord("my_heart_is_sad", SSSoundRegistry.MY_HEART_IS_SAD, 150);
    public static final RegistryObject<RecordItem> ONE_SUMMER_NIGHT = makeRecord("one_summer_night", SSSoundRegistry.ONE_SUMMER_NIGHT, 134);
    public static final RegistryObject<RecordItem> SILHOUETTES = makeRecord("silhouettes", SSSoundRegistry.SILHOUETTES, 163);
    public static final RegistryObject<RecordItem> SUNDOWN = makeRecord("sundown", SSSoundRegistry.SUNDOWN, 420);
    public static final RegistryObject<RecordItem> SOMEONE_YOU_LOVED = makeRecord("someone_you_loved", SSSoundRegistry.SOMEONE_YOU_LOVED, 182);
    public static final RegistryObject<RecordItem> THATS_MY_DESIRE = makeRecord("thats_my_desire", SSSoundRegistry.THATS_MY_DESIRE, 153);
    public static final RegistryObject<RecordItem> THRILLER = makeRecord("thriller", SSSoundRegistry.THRILLER, 357);
    public static final RegistryObject<RecordItem> THUG_SHAKE = makeRecord("thug_shake", SSSoundRegistry.THUG_SHAKE, 170);
    public static final RegistryObject<RecordItem> CORAL = makeRecord("coral", SSSoundRegistry.CORAL, 240);
    public static final RegistryObject<RecordItem> DONNA_THE_PRIMA_DONNA = makeRecord("donna_the_prima_donna", SSSoundRegistry.DONNA_THE_PRIMA_DONNA, 168);
    public static final RegistryObject<RecordItem> CANT_WE_BE_SWEETHEARTS = makeRecord("cant_we_be_sweethearts", SSSoundRegistry.CANT_WE_BE_SWEETHEARTS, 138);
    public static final RegistryObject<RecordItem> IVE_LIED = makeRecord("ive_lied", SSSoundRegistry.IVE_LIED, 118);
    public static final RegistryObject<Item> RADIO = ITEMS.register("radio", () -> new RadioItem(new Item.Properties().stacksTo(1).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));
    public static final RegistryObject<Item> DIVINE_FRUIT = ITEMS.register("divine_fruit", () -> new DivineFruitItem(new Item.Properties().stacksTo(64).tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)
            .rarity(ModRarities.DIVINE).food(new FoodProperties.Builder().nutrition(0).alwaysEat().build())));
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
    public static final RegistryObject<Item> DEMON_BLADE = ITEMS.register("demon_blade", () -> new DemonBladeItem(ModTiers.DEMON,2,-3.2f, new Item.Properties().rarity(ModRarities.DEMONIC).fireResistant().tab(SnappyStuffTabs.SNAPPY_STUFF_TAB)));


    public static RegistryObject<RecordItem> makeRecord(String name, RegistryObject<SoundEvent> sound, int seconds){
        return ITEMS.register(name, () -> new RecordItem(0,sound,new Item.Properties().rarity(ModRarities.MYTHICAL).fireResistant().tab(SnappyStuffTabs.SNAPPY_MUSIC_TAB),(seconds+2)*20));

    }
}
