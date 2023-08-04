package com.cdogsnappy.snappystuff.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;

import static net.minecraft.commands.arguments.EntityArgument.getEntity;

public class CreateKillMissionCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("createKillMission").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("entity", EntityArgument.entity())
                        .then(
                                Commands.argument("amount", IntegerArgumentType.integer())
                                        .then(
                                                Commands.argument("reward", ItemArgument.item(context))
                                                        .then(
                                                                Commands.argument("min", IntegerArgumentType.integer())
                                                                        .then(
                                                                                Commands.argument("max", IntegerArgumentType.integer())
                                                                                        .executes((c) -> {
                                                                                           // return makeMission(c, getEntity(c,"entity"),)
                                                                                            return 0;
                                                                                        })
                                                                        )
                                                        )
                                        )

                        ));
        root.then(builder);
    }

    //public static int makeMission(CommandContext<CommandSourceStack> c, Entity e, Item reward,)
}
