package com.cdogsnappy.snappystuff.radio;

import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.network.SoundStartPacketC2S;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.compress.utils.Lists;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class RadioClient {

    public static Map<UUID, SoundInstance> playingSounds = new HashMap<UUID, SoundInstance>();
    static int audioLength;
    static int currentAudioTime;
    public static List<CustomSoundEvent> ramblings = Lists.newArrayList();
    static Random rand = new Random();
    public static boolean isInStandby = false;
    private static SoundEvent STATIC;

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event){

        if(!isInStandby || event.phase == TickEvent.Phase.END){return;}
        if(++currentAudioTime >= audioLength){
            currentAudioTime = 0;
            playNewRambling(Minecraft.getInstance().player.getUUID());
        }
    }
    /**
     * @author Cdogsnappy
     * This is run whenever the server tells the client to play something
     */
    public static void playEntityBoundSound(SoundEvent soundEvent, UUID id, int entityId) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(entityId);
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        EntityBoundSoundInstance e = new EntityBoundSoundInstance(soundEvent,SoundSource.RECORDS,2,1,entity,level.random.nextLong());
        playingSounds.put(id,e);
        Minecraft.getInstance().getSoundManager().play(e);
    }

    /**
     * @author Cdogsnappy
     * @param id id of the audio player
     * This is run whenever a sound needs to stop, like a radio is disabled or broken.
     */
    public static void stopSound(UUID id){
        Minecraft.getInstance().getSoundManager().stop(playingSounds.remove(id));}

    public static void playNewRambling(UUID id){
        //CustomSoundEvent ramb = ramblings.get(rand.nextInt(ramblings.size()));
        audioLength = 101;
        SnappyNetwork.sendToServer(new SoundStartPacketC2S(id,STATIC,Minecraft.getInstance().player.level.dimension()));
        currentAudioTime = 0;

    }

    /**
     * Plays 3 seconds of static, called 4 seconds before a song ends on the radio.
     */
    public static void playStatic(UUID id){
        SnappyNetwork.sendToServer(new SoundStartPacketC2S(id,STATIC,Minecraft.getInstance().player.level.dimension()));
    }
    public static void init(){
        STATIC = SSSoundRegistry.STATIC.get();
    }
}
