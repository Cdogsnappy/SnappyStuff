package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.spawn.PlayerSpawn;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
public class AddCustomSpawnCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("addcustomspawn").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("position", Vec3Argument.vec3())
                        .executes(c -> {
                            return addSpawn(c, Vec3Argument.getVec3(c,"position"));
                        })
        );
        root.then(builder);
    }
    public static int addSpawn(CommandContext<CommandSourceStack> c, Vec3 vec){
        if(Math.abs(vec.x) > 29999997 || Math.abs(vec.y) > 29999997 || Math.abs(vec.z) > 29999997){
            c.getSource().sendFailure(Component.literal("Invalid Position"));
            return -1;
        }
        for(BlockPos pos : PlayerSpawn.possibleSpawns){
            if(pos.getX() == vec.x && pos.getY() == vec.y && pos.getZ() == vec.z){
                c.getSource().sendFailure(Component.literal("This custom spawn already exists."));
                return -2;
            }
        }
        PlayerSpawn.possibleSpawns.add(new BlockPos(vec.x,vec.y,vec.z));
        c.getSource().sendSuccess(Component.literal("Successfully added custom spawn to pool, list: " + PlayerSpawn.possibleSpawns.toString()), false);
        return 0;
    }
}