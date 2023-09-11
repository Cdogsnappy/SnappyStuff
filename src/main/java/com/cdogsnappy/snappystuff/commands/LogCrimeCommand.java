package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.court.CourtCase;
import com.cdogsnappy.snappystuff.court.Crime;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class LogCrimeCommand {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("concludeCase").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("id", IntegerArgumentType.integer())
                        .then(
                                Commands.argument("offense", StringArgumentType.string())
                                        .then(
                                                Commands.argument("victim", StringArgumentType.string())
                                                        .executes(c -> {
                                                            return logCrime(c, IntegerArgumentType.getInteger(c,"id"), StringArgumentType.getString(c,"offense"), StringArgumentType.getString(c,"victim"));
                                                        })
                                        ).executes(c -> {
                                            return logCrime(c, IntegerArgumentType.getInteger(c,"id"), StringArgumentType.getString(c,"offense"), null);
                                        })));
        root.then(builder);
    }

    public static int logCrime(CommandContext<CommandSourceStack> c, int id, String offense, String victim){
        if(CourtCase.caseArchive.containsKey(id)){
            c.getSource().sendFailure(Component.literal("Court case with id " + id + " does not exist."));
            return -2;
        }
        CourtCase courtCase = CourtCase.caseArchive.get(id);
        if(victim == null){
            courtCase.addCrime(new Crime(offense, courtCase.getSeverity(), null, courtCase.getParticipant(1).getUUID()));
            return 0;
        }
        if(CitizenData.citizenRegistry.containsKey(victim)) {
            courtCase.addCrime(new Crime(offense, courtCase.getSeverity(), CitizenData.citizenRegistry.get(victim).getUUID(), courtCase.getParticipant(1).getUUID()));
            return 0;
        }
        c.getSource().sendFailure(Component.literal("This victim doesn't exist!"));
        return -1;
    }


}
