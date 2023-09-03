package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.spawn.PlayerSpawn;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class RemoveCustomSpawnCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("removecustomspawn").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("position", Vec3Argument.vec3())
                        .executes(c -> {
                            return removeSpawn(c, Vec3Argument.getVec3(c,"position"));
                        })
        );
        root.then(builder);
    }
    public static int removeSpawn(CommandContext<CommandSourceStack> c, Vec3 vec){
        PlayerSpawn.possibleSpawns.forEach((p) -> {
            if(p.getX() == vec.x && p.getY() == vec.y && p.getZ() == vec.z){
                PlayerSpawn.possibleSpawns.remove(p);
                PlayerSpawn.rerollSpawns(p);
                }
        });
        c.getSource().sendSuccess(Component.literal("Successfully removed custom spawn from pool, list: " + PlayerSpawn.possibleSpawns.toString()), false);
        return 0;
    }
}
