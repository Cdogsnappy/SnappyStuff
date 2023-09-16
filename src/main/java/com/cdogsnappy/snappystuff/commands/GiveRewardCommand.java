package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GiveRewardCommand {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> root, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("giveReward").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("player", EntityArgument.player())
                        .then(
                                Commands.argument("item", ItemArgument.item(context))
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(c -> {
                                                    return giveReward(c, EntityArgument.getPlayer(c, "player"), ItemArgument.getItem(c, "item").getItem(), IntegerArgumentType.getInteger(c, "amount"));
                                                })
                                        )));
        root.then(builder);
    }

    public static int giveReward(CommandContext<CommandSourceStack> c, Player p, Item i, int amount){
        List<ItemStack> rewards = QuestHandler.playerQuestData.get(p.getUUID()).rewards;
        while(amount > 0){
            ItemStack stack = new ItemStack(i,Math.min(i.getMaxStackSize(),amount));
            amount-=Math.min(i.getMaxStackSize(),amount);
            rewards.add(stack);
        }
        return 0;
    }
}
