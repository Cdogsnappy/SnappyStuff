package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.arguments.EntityArgument.getPlayer;

public class ViewRadioSongsCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("getSongs").requires(c -> c.hasPermission(1))
                .executes(c -> {
                    return listSongs(c);
                });
        root.then(builder);
    }

    public static int listSongs(CommandContext<CommandSourceStack> c) {
        c.getSource().sendSystemMessage(Component.literal(RadioHandler.music.toString()));
        return 0;
    }
}