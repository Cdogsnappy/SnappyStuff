package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.spawn.PlayerSpawn;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class RerollCustomSpawnsCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("rerollspawns").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("player", EntityArgument.player())
                        .executes(c -> {
                            return PlayerSpawn.rerollSpawns(EntityArgument.getPlayer(c,"player").getUUID());
                        })
        ).executes(c -> {
            return PlayerSpawn.rerollSpawns();
        });
        root.then(builder);
    }

}
