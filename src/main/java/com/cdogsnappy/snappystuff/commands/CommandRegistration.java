package com.cdogsnappy.snappystuff.commands;

import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;

public class CommandRegistration {
    public static void registerCommands(RegisterCommandsEvent e){
        var builder = Commands.literal("snappy");
        EndorseCommand.register(builder);
        ChangeKarmaCommand.register(builder);
        CreateKillMissionCommand.register(builder, e.getBuildContext());
        CreateCollectMissionCommand.register(builder,e.getBuildContext());
        CreateBlockMissionCommand.register(builder,e.getBuildContext());
        GetKarmaCommand.register(builder);
        GiveRewardCommand.register(builder, e.getBuildContext());
        PrintRewardsCommand.register(builder);
        e.getDispatcher().register(builder);
    }
}
