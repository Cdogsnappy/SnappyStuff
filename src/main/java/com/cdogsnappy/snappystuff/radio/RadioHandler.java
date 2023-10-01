package com.cdogsnappy.snappystuff.radio;

import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.network.*;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.*;

@Mod.EventBusSubscriber
public class RadioHandler{
    public static List<Entity> listeners = Lists.newArrayList();
    public static List<Entity> standbyListeners = Lists.newArrayList();
    public static List<RecordItem> uploadableMusic = Lists.newArrayList();
    public static List<ItemStack> music = Lists.newArrayList();
    public static List<CustomSoundEvent> casts = Lists.newArrayList();
    static int currentAudioTime = 0;
    static int audioLength = 0;
    static boolean audioPlaying = false;
    static boolean justPlayedMusic = false;
    static RecordItem lastPlayed;
    static Random rand = new Random();
    static boolean sentWarning = false;
    /**
     * @author Cdogsnappy
     * runs every tick, keeping track of how far along the radio is in playing a song.
     */
    public static void onTick(TickEvent.ServerTickEvent event){
        if(++currentAudioTime >= audioLength || !audioPlaying){
            audioPlaying = false;
            currentAudioTime = 0;
            onloadListeners();
            playSomething();
        }
        else if(audioLength - currentAudioTime <= 104 && !sentWarning){
            //Send standbyListeners a warning that they can listen to music soon
            standbyListeners.forEach((e) -> {
                if(e instanceof Player){
                    SnappyNetwork.sendToNearbyPlayers(new MusicWarningPacket(e.getUUID()),e.position(),e.level.dimension());
                }
            });
            sentWarning = true;
        }
    }

    //We don't want phantom listeners
    @SubscribeEvent
    public static void onPlayerLogOff(PlayerEvent.PlayerLoggedOutEvent event){
        Player p = event.getEntity();
            listeners.remove(p);
            standbyListeners.remove(p);
            SnappyNetwork.sendToPlayer(new StandbyResetPacket(false),(ServerPlayer)p);
            SnappyNetwork.sendToNearbyPlayers(new SoundStopPacketS2C(p.getUUID()),p.position(),p.level.dimension());
            //REMEMBER TO CLEAR PLAYERS SOUNDMANAGER
    }
    /**
     * @author Cdogsnappy
     * Loads the uploadable songs into the list of music
     */
    public static void init(){
        ForgeRegistries.ITEMS.getValues().forEach((i) -> {
            if(i instanceof RecordItem){
                uploadableMusic.add((RecordItem)i);
            }
        });
        music.add(new ItemStack(ModItems.ONE_SUMMER_NIGHT.get()));
        music.add(new ItemStack(ModItems.CORAL.get()));
    }
    /**
     * Run when a song ends, we want the standby listeners to start listening to music.
     */
    public static void onloadListeners(){
        listeners.addAll(standbyListeners);
        standbyListeners.clear();
    }
    public static void playSomething(){
        SoundEvent e;
        int tickLength = 0;
        int ind = rand.nextInt(music.size());
        while(music.get(ind).is(lastPlayed)){ind = rand.nextInt(music.size());}
        lastPlayed = ((RecordItem)music.get(ind).getItem());
        e = lastPlayed.getSound();
        tickLength = lastPlayed.getLengthInTicks();
        /*
        if(justPlayedMusic){
            int ind = rand.nextInt(casts.size());
            e = casts.get(ind).sound;
            tickLength = casts.get(ind).tickLength;
        }
        else{
            e = ((RecordItem)music.get(rand.nextInt(music.size())).getItem()).getSound();
            tickLength = ((RecordItem)music.get(rand.nextInt(music.size())).getItem()).getLengthInTicks();
        }
         */
        justPlayedMusic = !justPlayedMusic;
        listeners.forEach((en) -> {
            SnappyNetwork.sendToNearbyPlayers(new SoundStartPacketS2C(en.getUUID(),e,en.getId()),en.position(),en.level.dimension());
        });
        audioLength = tickLength;
        currentAudioTime = 0;
        audioPlaying = true;
        sentWarning = false;
    }

    public static CompoundTag save(CompoundTag p_77763_) {
        ListTag tag = new ListTag();
        music.forEach((m) -> {
            tag.add(m.save(new CompoundTag()));
        });
        p_77763_.put("music",tag);

        return p_77763_;
    }
    public static void load(CompoundTag tag) {
        ListTag lTag = (ListTag)tag.get("music");
        for(int j = 0; j<lTag.size(); ++j){
            music.add(ItemStack.of(lTag.getCompound(j)));
        }
    }
    public static void uploadSong(RecordItem song){
        for(ItemStack s : RadioHandler.music){
            if(s.is(song)){
                return;
            }
        }
        RadioHandler.music.add(new ItemStack(song));
    }

}

