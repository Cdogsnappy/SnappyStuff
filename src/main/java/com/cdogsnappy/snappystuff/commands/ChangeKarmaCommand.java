package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.karma.KarmaPlayerInfo;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import com.cdogsnappy.snappystuff.karma.Karma;

import static net.minecraft.commands.arguments.EntityArgument.getPlayer;

public class ChangeKarmaCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("changeKarma").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("player", EntityArgument.player())
                        .then(
                                Commands.argument("newScore", FloatArgumentType.floatArg())
                                        .executes(c -> {
                                            return changeKarma(c, getPlayer(c,"player"), FloatArgumentType.getFloat(c,"newScore"));
                                        })
        ));
        root.then(builder);
    }
    public static int changeKarma(CommandContext<CommandSourceStack> c, Player player, float newKarma){
        Karma.setScore(player,newKarma);
        return 0;
    }
}
