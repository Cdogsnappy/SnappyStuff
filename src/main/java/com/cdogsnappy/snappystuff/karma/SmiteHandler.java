package com.cdogsnappy.snappystuff.karma;


import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.List;
import java.util.Random;
@Mod.EventBusSubscriber
public class SmiteHandler {
    public static double threshold = .0053333;
    static int ticks = 0;

    enum Wraths{
        FIRE,
        LIGHTNING,
        FAMINE;
    }
    enum Graces{
        GOLDEN_APPLE,
        HUNGER_FILL,
        POSITIVE_BUFF;
    }
    static Wraths[] wraths = {Wraths.FIRE,Wraths.LIGHTNING,Wraths.FAMINE};
    static Graces[] graces = {Graces.GOLDEN_APPLE,Graces.HUNGER_FILL,Graces.POSITIVE_BUFF};
    static MobEffect[] effects = {MobEffects.ABSORPTION,MobEffects.REGENERATION,MobEffects.DAMAGE_BOOST};
    protected static Random rand = new Random();

    /**
     * Counts up ticks until a threshold is met and then calls a judge event and updates endorsement cooldowns
     * @author Cdogsnappy
     * @param event ServerTickEvent
     */
    public static void onTick(TickEvent.ServerTickEvent event){
        ticks++;
        if (ticks >= 400) {
            SmiteHandler.judge(event.getServer());
            EndorsementHandler.updateCooldowns(event.getServer());
            ticks = 0;
        }

    }

    /**
     * Takes all players and rolls a random chance to judge them. If their karma is negative they are smited and if positive they are
     * graced.
     * @author Cdogsnappy
     * @param server The minecraft server
     */
    public static void judge(MinecraftServer server) {
        List<ServerPlayer> players =  server.getPlayerList().getPlayers();
        for (ServerPlayer p : players) {
            int score = Karma.karmaScores.get(p.getUUID()).getScore();
            if(rand.nextFloat() <= Math.max(Math.abs(score),1)*threshold){
                if(score<0){
                    smite(p,p.getLevel());
                }
                else{
                    grace(p,p.getLevel());
                }
            }

        }

    }

    /**
     * Smites a player with some effect
     * @author Cdogsnappy
     * @param p The player
     * @param w The world
     */
    public static void smite(ServerPlayer p, ServerLevel w){
        int wr = rand.nextInt(3);
        switch(wraths[wr]) {
            case FIRE:
                p.setSecondsOnFire(12);
                w.playSound((ServerPlayer)null, p.position().x, p.position().y, p.position().z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
                break;


            case LIGHTNING:
                LightningBolt e = new LightningBolt(EntityType.LIGHTNING_BOLT, w);
                e.setPos(p.position());
                w.addFreshEntity(e);
                p.setHealth(p.getHealth()-8.0f);
                break;

            case FAMINE:
                p.causeFoodExhaustion(80);
                w.playSound((ServerPlayer)null, p.position().x, p.position().y, p.position().z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + rand.nextFloat() * 0.2F);
                break;

            default:
                break;
        }

    }

    /**
     * Graces a player with some effect
     * @author Cdogsnappy
     * @param p The player
     * @param w The world
     */
    public static void grace(ServerPlayer p, ServerLevel w){
        int gr = rand.nextInt(3);
        switch(graces[gr]){
            case GOLDEN_APPLE:
                p.addItem(new ItemStack(Items.GOLDEN_APPLE,3));
                break;
            case HUNGER_FILL:
                p.getFoodData().setFoodLevel(20);
                p.getFoodData().setSaturation(5.0f);
                break;
            case POSITIVE_BUFF:
                int buff = rand.nextInt(effects.length);
                p.addEffect(new MobEffectInstance(effects[buff],12000));
                break;
            default:
                break;

        }
        w.playSound((ServerPlayer)null, p.position().x, p.position().y, p.position().z, SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 10000.0F, 0.8F + rand.nextFloat() * 0.2F);

    }
}

