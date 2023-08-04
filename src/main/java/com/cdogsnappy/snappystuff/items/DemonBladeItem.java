package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.karma.Karma;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class DemonBladeItem extends SwordItem {
    private float currKarma;
    private float currDamage;
    private MobEffect[] demonicEffects = {MobEffects.CONFUSION,MobEffects.HUNGER, MobEffects.WEAKNESS, MobEffects.MOVEMENT_SLOWDOWN};

    public DemonBladeItem(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    /**
     * @author Cdogsnappy
     * Recalculates the damage of the blade if the wielders karma isn't consistent.
     * @param stack  The Item being used
     * @param player The player that is attacking
     * @param entity The entity being attacked
     * @return false, we are not cancelling the attack.
     */
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity){
        float karma;
        if((karma = Karma.getScore(player.getUUID())) != currKarma){
            currKarma = karma;
            calculateDamage();
        }
        /**
         * We add effects in tiers
         */
        if(karma > 0){return false;}//We don't want to add effects if the wielder has positive karma!
        if(entity instanceof LivingEntity){
            LivingEntity lE = (LivingEntity)entity;
            int numEffects = (int)(currKarma/30);
            for(int j = 0; j < numEffects; ++j){//We add a new effect at each "tier" of bad karma: -30, -60, -90,...
                lE.addEffect(new MobEffectInstance(demonicEffects[j],200));
            }
        }
        return false;
    }
    @Override
    public float getDamage(){return currDamage;}

    /**
     * @author Cdogsnappy
     * Adjusts a given blade's damage to the karma of the player wielding it
     */
    private void calculateDamage(){
        if(currKarma >= -40){//Anyone with karma greater than -40 damage cannot effectively do damage with this.
            currDamage = 0;
        }
        else{
            currDamage = (float) Math.pow(-currKarma - 40,.71);//Arbitrary scaling factor, can be changed
        }
    }
}
