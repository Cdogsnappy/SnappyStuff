package com.cdogsnappy.snappystuff.items;

import com.cdogsnappy.snappystuff.karma.Karma;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public class DivineFruitItem extends Item {
    static UUID divineModifier = UUID.fromString("divine_fruit_modifier");

    public DivineFruitItem(Properties p_41383_) {
        super(p_41383_);
    }


    /**
     * Need to check if the fruit was eaten, if so then update the health of the player
     * @param level world
     * @param player player
     * @param hand hand holding item
     * @return the result from Item use
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> res = super.use(level,player,hand);
        if(res == InteractionResultHolder.consume(player.getItemInHand(hand))){
            attemptAugmentHealth(player);
        }
        return res;
    }

    public static void attemptAugmentHealth(Player player){
        if(Karma.getScore(player.getUUID()) <=0) {
            player.sendSystemMessage(Component.literal("A foul taste fills your mouth. YOU ARE UNWORTHY OF SUCH A GIFT."));
        }
        else{
            player.sendSystemMessage(Component.literal("A blissful taste fills your mouth. ACCEPT THIS GIFT."));
        }
            CompoundTag tag = player.getPersistentData();
            tag.putInt("divine_fruit",tag.getInt("divine_fruit")+1);
            updateDivineHealth(player);

    }
    public static void updateDivineHealth(Player player){
        if(Karma.getScore(player.getUUID()) <= 0){
            return;
        }
        player.getAttribute(Attributes.MAX_HEALTH).removePermanentModifier(divineModifier);
        player.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(divineModifier,"divine_fruit.health",
                player.getPersistentData().getInt("divine_fruit"), AttributeModifier.Operation.ADDITION));
    }
}
