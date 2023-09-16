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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PrintRewardsCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("printRewards").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("player", EntityArgument.player())
                                .executes((c) -> {
                                    return printRewards(EntityArgument.getPlayer(c,"player"));
                                }));
        root.then(builder);
    }

    public static int printRewards(Player p){
        p.sendSystemMessage(Component.literal(QuestHandler.playerQuestData.get(p.getUUID()).rewards.toString()));
        return 0;
    }
}
