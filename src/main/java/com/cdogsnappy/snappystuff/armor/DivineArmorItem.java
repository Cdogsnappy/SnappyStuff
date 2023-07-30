package com.cdogsnappy.snappystuff.armor;

import com.cdogsnappy.snappystuff.items.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;
import java.util.UUID;

public class DivineArmorItem extends ArmorItem {
    //private Item[] armor = {ModItems.DIVINE_ARMOR_LEGS.get(),ModItems.DIVINE_ARMOR_CHEST.get(),ModItems.DIVINE_ARMOR_FEET.get(),ModItems.DIVINE_ARMOR_HELMET.get()};
    private UUID attrID = UUID.fromString("a0e88c02-21cc-427f-b584-24b1e16a928b");

    public DivineArmorItem(ArmorMaterial p_40386_, EquipmentSlot p_40387_, Properties p_40388_) {
        super(p_40386_, p_40387_, p_40388_);
    }

    /**
     * @author Cdogsnappy
     * update the armor modifiers every tick that an entire set is worn
     * @param stack the itemStack DivineArmorItem
     * @param level the dimension (WILL BE USEFUL)
     * @param player the player wearing the Divine Armor
     */

    /*
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player)
    {

        if(this.slot != EquipmentSlot.CHEST){
            return;
        }
        //Add any attributes you want the armor to affect to this array
        AttributeInstance[] divineEffects = {player.getAttribute(Attributes.ARMOR),player.getAttribute(Attributes.ARMOR_TOUGHNESS),player.getAttribute(Attributes.KNOCKBACK_RESISTANCE),player.getAttribute(Attributes.MOVEMENT_SPEED)};
        for(AttributeInstance instance : divineEffects){
            instance.removeModifier(attrID);
        }
        for(ItemStack i : player.getArmorSlots()){
            if(!ArrayUtils.contains(armor,i.getItem())){return;}
        }
        if (level.dimension() != Level.OVERWORLD){return;}//Check that we are in the overworld

        float[] instanceModifiers = {.4f,.2f,.1f,.2f};//Arbitrary, can be changed, these values impact the divine attribute modifiers, correspond to the attributes in divineEffects.
        float divineModifier = Math.min((float)player.getY()/100, 9.0f);
        for(int i = 0; i<divineEffects.length;i++){//DO NOT MODIFY THIS
            divineEffects[i].addTransientModifier(new AttributeModifier(attrID,
                    "divinearmor."+divineEffects[i].getAttribute().getDescriptionId(),
                    divineModifier*instanceModifiers[i], AttributeModifier.Operation.ADDITION));
        }
    }
    */


}
