package com.cdogsnappy.snappystuff.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

import static net.minecraft.commands.arguments.EntityArgument.getPlayer;

public class CourtConcludeCommand {
    /*
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("concludeCase").requires(c -> c.hasPermission(1));

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
    */

}
