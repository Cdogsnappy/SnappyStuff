package com.cdogsnappy.snappystuff.commands;

import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class CommandRegistration {
    public static void registerCommands(RegisterCommandsEvent e){
        var builder = Commands.literal("snappy");
        EndorseCommand.register(builder);
        ChangeKarmaCommand.register(builder);
        CourtCreateCommand.register(builder);
        CreateKillMissionCommand.register(builder, e.getBuildContext());
        LogCrimeCommand.register(builder);
        e.getDispatcher().register(builder);
    }
}
