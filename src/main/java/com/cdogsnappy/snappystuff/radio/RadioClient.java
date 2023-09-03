package com.cdogsnappy.snappystuff.radio;

import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import com.cdogsnappy.snappystuff.network.SoundStartPacketC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.compress.utils.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RadioClient {

    public static Map<UUID, SoundInstance> playingSounds = new ConcurrentHashMap<UUID, SoundInstance>();
    static int audioLength;
    static int currentAudioTime;
    public static List<CustomSoundEvent> ramblings = Lists.newArrayList();
    static Random rand = new Random();
    public static boolean isInStandby = true;
    private static final SoundEvent STATIC = null;

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event){
        if(!isInStandby){return;}
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
    public static void stopSound(UUID id){Minecraft.getInstance().getSoundManager().stop(playingSounds.remove(id));}

    public static void playNewRambling(UUID id){
        CustomSoundEvent ramb = ramblings.get(rand.nextInt(ramblings.size()));
        audioLength = ramb.tickLength;
        SnappyNetwork.sendToServer(new SoundStartPacketC2S(id,ramb.sound,Minecraft.getInstance().player.level.dimension()));

    }

    /**
     * Plays 3 seconds of static, called 4 seconds before a song ends on the radio.
     */
    public static void playStatic(UUID id){
        SnappyNetwork.sendToServer(new SoundStartPacketC2S(id,STATIC,Minecraft.getInstance().player.level.dimension()));
    }
}
