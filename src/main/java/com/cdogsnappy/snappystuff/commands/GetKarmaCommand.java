package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.karma.Karma;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import static net.minecraft.commands.arguments.EntityArgument.getPlayer;

public class GetKarmaCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("getKarma").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("player", EntityArgument.player())
                                .executes(c -> {
                                    return getKarma(c, getPlayer(c,"player"));
                                })
        );
        root.then(builder);
    }
    public static int getKarma(CommandContext<CommandSourceStack> c, Player player){
        c.getSource().sendSystemMessage(Component.literal(player.getName().getString() + "'s karma score: " + Karma.getScore(player.getUUID())));
        return 0;
    }
}
