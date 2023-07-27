package com.cdogsnappy.snappystuff.radio;

import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class RadioHandler {
    public static List<Player> listeners = new ArrayList<Player>();
    public static List<SoundInfo> musicLocations = new ArrayList<>();
    public static List<CustomSoundEvent> music = new ArrayList<CustomSoundEvent>();
    public static List<CustomSoundEvent> casts = new ArrayList<CustomSoundEvent>();
    public static Map<Player, SoundInstance> playingSounds = new ConcurrentHashMap<Player, SoundInstance>();
    static Random rand = new Random();

    static int currentAudioTime = 0;
    static int audioLength = 0;
    static boolean audioPlaying = false;
    static boolean justPlayedMusic = false;



    public static void onTick(){
        if(!audioPlaying){
            playSomething();
            return;
        }
        currentAudioTime++;
        if(currentAudioTime >= audioLength){
            audioPlaying = false;
            currentAudioTime = 0;
        }


    }

    //We don't want phantom listeners
    @SubscribeEvent
    public static void onPlayerLogOff(PlayerEvent.PlayerLoggedOutEvent event){
        Player p = event.getEntity();
            listeners.remove(p);
            playingSounds.remove(p);
    }

    //Radio users should still be active on the radio when they relog
    @SubscribeEvent
    public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event){
        Player p = event.getEntity();
        Optional<SlotResult> radStack = CuriosApi.getCuriosHelper().findFirstCurio(p, ModItems.RADIO.get());//Look for an equipped radio
        if(!radStack.isEmpty()){//If radio is equipped
            listeners.add(p);
        }
    }
    /*
    Call this when nothing is playing
    If a song just played, play an intermission(cast), otherwise play a song.
     */
    public static void playSomething(){
        playingSounds.clear();
        List<CustomSoundEvent> toPlay;
        int nextAudio;
        if(justPlayedMusic){//Determine from what list of sounds to grab from
            toPlay = casts;
            nextAudio = rand.nextInt(casts.size());



        }
        else{
            toPlay = music;
            nextAudio = rand.nextInt(music.size());
        }
        for(Player p : listeners){//Iterate through each player that is actively listening to the radio and create the sound for them.
            EntityBoundSoundInstance sound = new EntityBoundSoundInstance(toPlay.get(nextAudio).getSound(), SoundSource.RECORDS, 2, 1, p, 1);
            playingSounds.put(p,sound);
            Minecraft.getInstance().getSoundManager().play(sound);

        }
        audioLength = toPlay.get(nextAudio).getTickLength();
        justPlayedMusic = !justPlayedMusic;
        audioPlaying = true;
    }
    public static void init(){
        SoundEvent[] sounds = (SoundEvent[])SSSoundRegistry.SOUNDS.getEntries().toArray();
        for(SoundInfo info : musicLocations){
            music.add(new CustomSoundEvent(sounds[info.index],info.lengthInTicks));
        }
    }



}

