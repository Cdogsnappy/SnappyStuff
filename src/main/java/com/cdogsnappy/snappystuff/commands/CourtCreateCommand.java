package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.court.CourtCase;
import com.cdogsnappy.snappystuff.court.JuryHandler;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CourtCreateCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("createCase").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("Judge", StringArgumentType.string())
                        .then(
                                Commands.argument("Defendant", StringArgumentType.string())
                                                .then(
                                                        Commands.argument("Plaintiff", StringArgumentType.string())
                                                                .then(
                                                                        Commands.argument("DLawyer", StringArgumentType.string())
                                                                                .then(
                                                                                        Commands.argument("PLawyer", StringArgumentType.string())
                                                                                                .then(
                                                                                                        Commands.argument("Severity", IntegerArgumentType.integer())))
                                                                                .executes(c -> {
                                                                                    return createCourtCase(c, Arrays.asList(new String[]{StringArgumentType.getString(c, "Judge"),StringArgumentType.getString(c, "Defendant"),
                                                                                            StringArgumentType.getString(c, "Plaintiff"),StringArgumentType.getString(c, "DLawyer"),
                                                                                            StringArgumentType.getString(c, "PLawyer"), null, null, null}), IntegerArgumentType.getInteger(c,"Severity"));
                                                                                })
                                                                                                )
                                                                                )
                                                                )
                                                );



        root.then(builder);
    }

    public static int createCourtCase(CommandContext<CommandSourceStack> c, List<String> participants, int severity){
        participants.addAll(JuryHandler.callJury(participants));
        List<CitizenData> citizens = new ArrayList<>();
        for(String s : participants){
            if(!CitizenData.citizenRegistry.containsKey(s)){
                c.getSource().sendFailure(Component.literal("One of these participants doesn't exist!"));
                return -1;
            }
            citizens.add(CitizenData.citizenRegistry.get(s));
        }

        CourtCase courtCase = new CourtCase(citizens.toArray(new CitizenData[0]),severity);
        c.getSource().sendSystemMessage(Component.literal("Court case created with id " + courtCase.getCaseID()));
        return 0;
    }
}
