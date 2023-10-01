package com.cdogsnappy.snappystuff.sounds;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class SSSoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SnappyStuff.MODID);

    public static final RegistryObject<SoundEvent> AFRICA = createSoundEvent("africa");
    public static final RegistryObject<SoundEvent> BLACKBIRD = createSoundEvent("blackbird");
    public static final RegistryObject<SoundEvent> BRANDY_YOURE_A_FINE_GIRL = createSoundEvent("brandy_youre_a_fine_girl");
    public static final RegistryObject<SoundEvent> BROWN_EYED_GIRL = createSoundEvent("brown_eyed_girl");
    public static final RegistryObject<SoundEvent> CARRY_ON_MY_WAYWARD_SON = createSoundEvent("carry_on_my_wayward_son");
    public static final RegistryObject<SoundEvent> DANCING_IN_THE_DARK = createSoundEvent("dancing_in_the_dark");
    public static final RegistryObject<SoundEvent> DANCING_IN_THE_MOONLIGHT = createSoundEvent("dancing_in_the_moonlight");
    public static final RegistryObject<SoundEvent> DONT_FEAR_THE_REAPER = createSoundEvent("dont_fear_the_reaper");
    public static final RegistryObject<SoundEvent> DREAM_SWEET_IN_SEA_MAJOR = createSoundEvent("dream_sweet_in_sea_major");
    public static final RegistryObject<SoundEvent> DUST_IN_THE_WIND = createSoundEvent("dust_in_the_wind");
    public static final RegistryObject<SoundEvent> EAGLE = createSoundEvent("eagle");
    public static final RegistryObject<SoundEvent> FEELS_LIKE_THE_FIRST_TIME = createSoundEvent("feels_like_the_first_time");
    public static final RegistryObject<SoundEvent> FREE_FALLIN = createSoundEvent("free_fallin");
    public static final RegistryObject<SoundEvent> HAVE_YOU_EVER_SEEN_THE_RAIN = createSoundEvent("have_you_ever_seen_the_rain");
    public static final RegistryObject<SoundEvent> HERE_I_GO_AGAIN = createSoundEvent("here_i_go_again");
    public static final RegistryObject<SoundEvent> HOT_BLOODED = createSoundEvent("hot_blooded");
    public static final RegistryObject<SoundEvent> HOTEL_CALIFORNIA = createSoundEvent("hotel_california");
    public static final RegistryObject<SoundEvent> HOUSE_OF_THE_RISING_SUN = createSoundEvent("house_of_the_rising_sun");
    public static final RegistryObject<SoundEvent> HUNGRY_LIKE_THE_WOLF = createSoundEvent("hungry_like_the_wolf");
    public static final RegistryObject<SoundEvent> I_WANT_TO_KNOW_WHAT_LOVE_IS = createSoundEvent("i_want_to_know_what_love_is");
    public static final RegistryObject<SoundEvent> LISTEN_TO_THE_MUSIC = createSoundEvent("listen_to_the_music");
    public static final RegistryObject<SoundEvent> LONELY_IS_THE_NIGHT = createSoundEvent("lonely_is_the_night");
    public static final RegistryObject<SoundEvent> MANEATER = createSoundEvent("maneater");
    public static final RegistryObject<SoundEvent> MARGARITAVILLE = createSoundEvent("margaritaville");
    public static final RegistryObject<SoundEvent> MAYBE_IM_AMAZED = createSoundEvent("maybe_im_amazed");
    public static final RegistryObject<SoundEvent> OLD_TIME_ROCK_AND_ROLL = createSoundEvent("old_time_rock_and_roll");
    public static final RegistryObject<SoundEvent> PRIVATE_EYES = createSoundEvent("private_eyes");
    public static final RegistryObject<SoundEvent> RAMBLIN_MAN = createSoundEvent("ramblin_man");
    public static final RegistryObject<SoundEvent> RESONANCE = createSoundEvent("resonance");
    public static final RegistryObject<SoundEvent> RICH_GIRL = createSoundEvent("rich_girl");
    public static final RegistryObject<SoundEvent> ROCK_YOU_LIKE_A_HURRICANE = createSoundEvent("rock_you_like_a_hurricane");
    public static final RegistryObject<SoundEvent> SICKO_MODE = createSoundEvent("sicko_mode");
    public static final RegistryObject<SoundEvent> STAND_BY_ME = createSoundEvent("stand_by_me");
    public static final RegistryObject<SoundEvent> STUCK_IN_THE_MIDDLE_WITH_YOU = createSoundEvent("stuck_in_the_middle_with_you");
    public static final RegistryObject<SoundEvent> SWEET_HOME_ALABAMA = createSoundEvent("sweet_home_alabama");
    public static final RegistryObject<SoundEvent> TAKE_ME_HOME_TONIGHT = createSoundEvent("take_me_home_tonight");
    public static final RegistryObject<SoundEvent> THE_CHAIN = createSoundEvent("the_chain");
    public static final RegistryObject<SoundEvent> THE_JOKER = createSoundEvent("the_joker");
    public static final RegistryObject<SoundEvent> UNION_DIXIE = createSoundEvent("union_dixie");
    public static final RegistryObject<SoundEvent> WE_BUILT_THIS_CITY = createSoundEvent("we_built_this_city");
    public static final RegistryObject<SoundEvent> WERE_FINALLY_LANDING = createSoundEvent("were_finally_landing");
    public static final RegistryObject<SoundEvent> WHEEL_IN_THE_SKY = createSoundEvent("wheel_in_the_sky");
    public static final RegistryObject<SoundEvent> WOMAN = createSoundEvent("woman");
    public static final RegistryObject<SoundEvent> YOU_REALLY_GOT_ME = createSoundEvent("you_really_got_me");
    public static final RegistryObject<SoundEvent> YOUR_SONG = createSoundEvent("your_song");
    public static final RegistryObject<SoundEvent> METAMODERNITY = createSoundEvent("metamodernity");
    public static final RegistryObject<SoundEvent> AQUARIUM = createSoundEvent("aquarium");
    public static final RegistryObject<SoundEvent> EYE_OF_THE_TIGER = createSoundEvent("eye_of_the_tiger");
    public static final RegistryObject<SoundEvent> GODZILLA = createSoundEvent("godzilla");
    public static final RegistryObject<SoundEvent> HEART_ATTACK = createSoundEvent("heart_attack");
    public static final RegistryObject<SoundEvent> HOT_STUFF = createSoundEvent("hot_stuff");
    public static final RegistryObject<SoundEvent> LET_IT_PLEASE_BE_YOU = createSoundEvent("let_it_please_be_you");
    public static final RegistryObject<SoundEvent> MY_HEART_IS_SAD = createSoundEvent("my_heart_is_sad");
    public static final RegistryObject<SoundEvent> ONE_SUMMER_NIGHT = createSoundEvent("one_summer_night");
    public static final RegistryObject<SoundEvent> SILHOUETTES = createSoundEvent("silhouettes");
    public static final RegistryObject<SoundEvent> SUNDOWN = createSoundEvent("sundown");
    public static final RegistryObject<SoundEvent> SOMEONE_YOU_LOVED = createSoundEvent("someone_you_loved");
    public static final RegistryObject<SoundEvent> THATS_MY_DESIRE = createSoundEvent("thats_my_desire");
    public static final RegistryObject<SoundEvent> THRILLER = createSoundEvent("thriller");
    public static final RegistryObject<SoundEvent> THUG_SHAKE = createSoundEvent("thug_shake");
    public static final RegistryObject<SoundEvent> CORAL = createSoundEvent("coral");
    public static final RegistryObject<SoundEvent> DONNA_THE_PRIMA_DONNA = createSoundEvent("donna_the_prima_donna");
    public static final RegistryObject<SoundEvent> CANT_WE_BE_SWEETHEARTS = createSoundEvent("cant_we_be_sweethearts");
    public static final RegistryObject<SoundEvent> IVE_LIED = createSoundEvent("ive_lied");
    public static final RegistryObject<SoundEvent> STATIC = createSoundEvent("radio_static");

    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(SnappyStuff.MODID,soundName));
        return SOUNDS.register(soundName, () -> sound);
    }
}
