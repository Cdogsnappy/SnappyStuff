package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.karma.Karma;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.antlr.v4.runtime.misc.MultiMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static net.minecraft.world.damagesource.DamageSource.playerAttack;

public class DemonBladeItem extends SwordItem {
    private UUID DEMON_DAMAGE = UUID.fromString("48d38f4c-036c-4f2c-9e94-e740e988de6f");
    private Map<ItemStack,Float> currDamages = new HashMap<>();
    private MobEffect[] demonicEffects = {MobEffects.CONFUSION,MobEffects.HUNGER, MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN};

    public DemonBladeItem(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    /**
     * @author Cdogsnappy
     * Recalculates the damage of the blade if the wielders karma isn't consistent.
     * @param pStack  The Item being used
     * @param pTarget The player that is attacking
     * @param player The entity being attacked
     * @return whatever the super wants to do with the attack.
     */

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity player) {
        if(!(player instanceof Player)){return super.hurtEnemy(pStack,pTarget,player);}
        float karma = 0;
        float newKarma = 0;
        if((newKarma = Karma.getScore(player.getUUID())) >= -20){
            return true;
        }
        if(pStack.getTag().contains("demon_modifier")){
            CompoundTag dTag = pStack.getTagElement("demon_modifier");
            karma = dTag.getFloat("karma");
        }
        if(karma != newKarma){
            CompoundTag tag = new CompoundTag();
            tag.putFloat("karma",newKarma);
            tag.putFloat("damage",calculateDamage(newKarma));
            pStack.addTagElement("demon_modifier",tag);
        }
        /**
         * We add effects in tiers
         */
        int numEffects = Math.min(demonicEffects.length,(int)(karma/-30));
        for(int j = 0; j < numEffects; ++j){//We add a new effect at each "tier" of bad karma: -30, -60, -90,...
            pTarget.addEffect(new MobEffectInstance(demonicEffects[j],200), player);
        }
        /*
        if(karma < -100 && Math.random() >= 0 && pTarget.getLevel().dimension() != Level.NETHER){
            pTarget.changeDimension(pAttacker.getServer().getLevel(Level.NETHER));
        }
         */
        return super.hurtEnemy(pStack,pTarget,player);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        Multimap<Attribute, AttributeModifier> attrMap = super.getAttributeModifiers(equipmentSlot,stack);
        attrMap.forEach((k,v) -> {
            builder.put(k,v);
        });
        if(equipmentSlot == EquipmentSlot.MAINHAND){
            float damage = 0;
            if(stack.getTag().contains("demon_modifier")){
                damage = stack.getTagElement("demon_modifier").getFloat("damage");
            }
            builder.put(Attributes.ATTACK_DAMAGE,new AttributeModifier(DEMON_DAMAGE,"demon",damage, AttributeModifier.Operation.ADDITION));}
        return builder.build();
    }
    @Override
    public float getDamage(){return this.getDamage();}

    /**
     * @author Cdogsnappy
     * Adjusts a given blade's damage to the karma of the player wielding it
     */
    private float calculateDamage(float karma){
        if(karma >= -20){
            return -10;
        }
        else {
            return Math.max((float) Math.pow(-karma - 20, .71) - 10, 0);//Arbitrary scaling factor, can be changed
        }
    }
}
