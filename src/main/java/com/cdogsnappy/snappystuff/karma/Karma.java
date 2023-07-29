package com.cdogsnappy.snappystuff.karma;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;
import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber
public class Karma{
    public static UUID id = UUID.fromString("c0bef565-35f6-4dc5-bb4c-3644c382e6ce");
    public static HashMap<UUID, KarmaPlayerInfo> karmaScores = new HashMap<UUID, KarmaPlayerInfo>();

    public static void init() {


        karmaScores.put(UUID.fromString("3af72133-2a77-4ddc-8ed8-b6379c7900cd"), new KarmaPlayerInfo(-20, -5.0f));
        karmaScores.put(UUID.fromString("1e80636b-9665-4e4f-a1b5-e3ecd74a7bcc"), new KarmaPlayerInfo(-5, -1.0f));
        karmaScores.put(UUID.fromString("82a7ffbe-7b11-4015-a124-9ecb07a78223"), new KarmaPlayerInfo(-21, -5.0f));
        karmaScores.put(UUID.fromString("b5b549c9-5e81-4688-bcf3-4ebe7bd4a409"), new KarmaPlayerInfo(-9, -2.0f));
        karmaScores.put(UUID.fromString("8dc4617f-6efd-4911-ab19-be96281392dc"), new KarmaPlayerInfo(18, 5.0f));
        karmaScores.put(UUID.fromString("9c215c04-6200-4b4c-9316-bf56ddb1aca6"), new KarmaPlayerInfo(12, 3.0f));
        karmaScores.put(UUID.fromString("4984f059-205b-46e6-a0b4-5261e75fdc5e"), new KarmaPlayerInfo(2, 1.0f));
        karmaScores.put(UUID.fromString("50d88d5f-3d7f-4146-a5f2-85ad1298c42c"), new KarmaPlayerInfo(-9, -2.0f));
        karmaScores.put(UUID.fromString("32d3077d-6dc6-4063-acaa-4c6c38d65797"), new KarmaPlayerInfo(-9, -2.0f));
        karmaScores.put(UUID.fromString("47eb09ae-82bd-4dc1-90a6-7450825b48b7"), new KarmaPlayerInfo(5, 1.0f));
        karmaScores.put(UUID.fromString("4c08423e-ddbf-4105-889e-abbe5101c9c7"), new KarmaPlayerInfo(10, 3.0f));
        karmaScores.put(UUID.fromString("f3e63fb6-8491-4bf2-b7e7-34bb4b292750"), new KarmaPlayerInfo(-1, 0.0f));
    }

    public static void playerCheck(PlayerEvent event){
        Player player = event.getEntity();
        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (!(attr.getModifier(id) == null)) {
            return;
        }

        if (!Karma.karmaScores.containsKey(player.getUUID())) {
            Karma.karmaScores.put(player.getUUID(), new KarmaPlayerInfo());
        }
        attr.addPermanentModifier(new AttributeModifier(id, "karma.healthModifier",
                Karma.karmaScores.get(player.getUUID()).getHealth(), AttributeModifier.Operation.ADDITION));
        player.setHealth(player.getMaxHealth());


    }

    @SubscribeEvent
    public static void onPlayerKilled(LivingDeathEvent event){
        if(event.getEntity() instanceof Player && event.getEntity().getKillCredit() instanceof Player murderer){
            Karma.setScore(murderer.getUUID(),Karma.getScore(murderer.getUUID()) - 10);

        }
    }

    public static void setHealth(UUID id, float newHealth){
        KarmaPlayerInfo info = karmaScores.get(id);
        info.score = newHealth;
        karmaScores.put(id, info);
    }
    public static void setScore(UUID id, float score){
        KarmaPlayerInfo info = karmaScores.get(id);
        info.score = score;
        karmaScores.put(id, info);
    }
    public static void setEndorsements(UUID id, int endorsements){
        KarmaPlayerInfo i = karmaScores.get(id);
        i.numEndorsements = endorsements;
        karmaScores.put(id, i);
    }
    public static void setEndorsed(UUID id, int endorsed){
        KarmaPlayerInfo i = karmaScores.get(id);
        i.numEndorsed = endorsed;
        karmaScores.put(id, i);
    }
    public static void updateEndorsed(UUID seid, UUID reid){
        KarmaPlayerInfo i = karmaScores.get(seid);
        i.playersEndorsed[ArrayUtils.indexOf(i.playersEndorsed,null)] = new EndorsementInfo(reid);
        i.numEndorsed++;
        karmaScores.put(seid,i);
    }
    public static void setKarmaInfo(UUID id, KarmaPlayerInfo info){karmaScores.put(id,info);}
    public static float getHealth(UUID id){
        return karmaScores.get(id).health;
    }
    public static float getScore(UUID id) {return karmaScores.get(id).score;}
    public static int getEndorsements(UUID id){return karmaScores.get(id).numEndorsements;}
    public static int getEndorsed(UUID id){return karmaScores.get(id).numEndorsed;}
    public static KarmaPlayerInfo getKarmaInfo(UUID id){
        return karmaScores.get(id);
    }
}


