package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.karma.Karma;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;

import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

@Mod.EventBusSubscriber
public class DivineFruitItem extends Item{
    private static UUID divineModifier = UUID.fromString("e4bac0b0-77e0-4303-a286-f886e9316b7a");

    public DivineFruitItem(Properties p_41383_) {
        super(p_41383_);
    }


    /**
     * Need to check if the fruit was eaten, if so then update the health of the player
     * @param event the event
     */
    @SubscribeEvent
    public static void onFruitEat(LivingEntityUseItemEvent.Finish event){
        if(!event.getEntity().level.isClientSide && event.getItem().is(ModItems.DIVINE_FRUIT.get())){
            attemptAugmentHealth((Player)event.getEntity());
        }

    }


    /**
     * Attempts to augment a players health, will always increment the player's health but will only have impact with high enough karma
     * @param player the player
     */

    public static void attemptAugmentHealth(Player player){
        if(Karma.getScore(player.getUUID()) <=0) {
            player.sendSystemMessage(Component.literal("A foul taste fills your mouth. YOU ARE UNWORTHY OF SUCH A GIFT."));
        }
        CompoundTag tag = player.getPersistentData();
        if(tag.getInt("divine_fruit") >= 20){
            player.sendSystemMessage(Component.literal("It seems you cannot absorb anymore power from the divine fruit."));
            return;
        }
        else{
            player.sendSystemMessage(Component.literal("A blissful taste fills your mouth. ACCEPT THIS GIFT."));
            player.level.playSound((ServerPlayer)null, player.position().x, player.position().y, player.position().z, SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 10000.0F, 0.8F);
        }

            tag.putInt("divine_fruit",tag.getInt("divine_fruit")+2);
            updateDivineHealth(player);

    }

    /**
     * Updates a player's divine fruit health modifier. This modifier isn't applied if the player's karma is too low
     * @param player the player
     */
    public static void updateDivineHealth(Player player){
        if(Karma.getScore(player.getUUID()) <= 0){
            return;
        }
        player.getAttribute(Attributes.MAX_HEALTH).removePermanentModifier(divineModifier);
        player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(divineModifier,"divine_fruit.health",
                player.getPersistentData().getInt("divine_fruit"), AttributeModifier.Operation.ADDITION));
        player.setHealth(Math.min(player.getHealth()+2,player.getMaxHealth()));
    }
    public static void addTag(Player player){
        if(!player.getPersistentData().contains("divine_fruit")){
            player.getPersistentData().putInt("divine_fruit",0);
        }
    }
}
