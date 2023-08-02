package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.court.CourtCase;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.minecraft.commands.arguments.EntityArgument.getPlayer;

public class CourtConcludeCommand {

    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("concludeCase").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("id", IntegerArgumentType.integer())
                    .then(
                        Commands.argument("status", StringArgumentType.string())
                                .then(
                                        Commands.argument("verdict", StringArgumentType.string())
                                                .then(
                                                        Commands.argument("punishment", StringArgumentType.string())
                                                                .executes(c -> {
                                                                    return concludeCase(c, IntegerArgumentType.getInteger(c,"id"), getString(c,"status"), getString(c,"verdict"),getString(c,"punishment"));
                                                        })
                                        )
                        )));
        root.then(builder);
    }

    public static int concludeCase(CommandContext<CommandSourceStack> c, int id, String status, String verdict, String punishment){
        if(CourtCase.caseArchive.containsKey(id)){
            c.getSource().sendFailure(Component.literal("Court case with id " + id + " does not exist."));
            return -2;
        }
        CourtCase.Status stat = switch(status){
            case "complete" -> CourtCase.Status.COMPLETED;
            case "mistrial" -> CourtCase.Status.MISTRIAL;
            default -> null;
        };
        CourtCase.Verdict verd = switch(verdict){
            case "guilty" -> CourtCase.Verdict.GUILTY;
            case "innocent" -> CourtCase.Verdict.NOT_GUILTY;
            case "mistrial" -> CourtCase.Verdict.MISTRIAL;
            default -> null;
        };
        CourtCase courtCase = CourtCase.caseArchive.get(id);
        courtCase.completeCase(stat,verd,punishment,null);
        return 0;
    }


}
