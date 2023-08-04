package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.quest.mission.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.arguments.EntitySummonArgument.getSummonableEntity;

public class CreateBlockMissionCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("createBlockMission").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("block", ItemArgument.item(context))
                        .then(
                                Commands.argument("amount", IntegerArgumentType.integer())
                                        .then(
                                                Commands.argument("reward", ItemArgument.item(context))
                                                        .then(
                                                                Commands.argument("min", IntegerArgumentType.integer())
                                                                        .then(
                                                                                Commands.argument("max", IntegerArgumentType.integer())
                                                                                        .executes((c) -> {
                                                                                            try {
                                                                                                return makeMission(c, ItemArgument.getItem(c,"block"),IntegerArgumentType.getInteger(c,"amount"),
                                                                                                        ItemArgument.getItem(c,"reward"),IntegerArgumentType.getInteger(c,"min"),
                                                                                                        IntegerArgumentType.getInteger(c,"max"));
                                                                                            } catch (IOException e) {
                                                                                                throw new RuntimeException(e);
                                                                                            }
                                                                                        })
                                                                        )
                                                        )
                                        )

                        ));
        root.then(builder);
    }

    public static int makeMission(CommandContext<CommandSourceStack> c, ItemInput b, int amount, ItemInput item, int min, int max) throws CommandSyntaxException, IOException {
        try {
            if(!(b.getItem() instanceof BlockItem)){
                c.getSource().sendFailure(Component.literal("This is not a block, try a collection mission."));
                return -3;
            }
            Block block = ((BlockItem)b.getItem()).getBlock();
            if (amount <= 0) {
                c.getSource().sendFailure(Component.literal("target amount must be positive."));
                return -2;
            }
            if (min <= 0 || max < min) {
                c.getSource().sendFailure(Component.literal("Invalid reward range."));
                return -1;
            }
            List<DailyReward> rewards = new ArrayList<>();
            rewards.add(new DailyReward(item.getItem(),min,max));
            DailyMission mission = new DailyMission(new BlockMission(block, amount), rewards);
            MissionJSONHandler.writeMission(mission);
            MissionHandler.dailyMissionList.add(mission);
            c.getSource().sendSuccess(Component.literal("Successfully wrote mission to quest system."), false);
            return 0;
        }
        catch(Exception ex){
            c.getSource().sendFailure(Component.literal(ex.getMessage()));
            return -4;
        }

    }
}
