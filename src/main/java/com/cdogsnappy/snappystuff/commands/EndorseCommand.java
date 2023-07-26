package com.cdogsnappy.snappystuff.commands;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.karma.EndorsementHandler;
import com.cdogsnappy.snappystuff.karma.EndorsementInfo;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

public class EndorseCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("endorse").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("player", EntityArgument.player())
                        .executes(c -> {
                            return endorse(c, EntityArgument.getPlayer(c,"player"), c.getSource().getPlayer());
                        })
        );
        root.then(builder);
    }

    public static int endorse(CommandContext<CommandSourceStack> c, Player praised, Player praiser){
        UUID reid = praised.getUUID();//receiver
        UUID seid = praiser.getUUID();//sender
        if(SnappyStuff.k.getEndorsed(seid) == 3){//CHECK THAT PRAISER HAS ENDORSEMENTS LEFT
            c.getSource().sendFailure(Component.literal("You've already used all of your endorsements today!"));
            return -1;
        }
        EndorsementInfo[] e = SnappyStuff.k.getKarmaInfo(seid).getPlayersEndorsed();

        for(int i = 0; i<3; i++) {//CHECK THAT PRAISER HASN'T ALREADY ENDORSED THIS PLAYER
            if(e[i] == null){
                continue;
            }
            if (e[i].getID() == reid) {
                c.getSource().sendFailure((Component.literal("You've already praised " + praised.getName().getString() + " today!")));
                return -2;
            }
        }
        SnappyStuff.k.setEndorsements(reid,SnappyStuff.k.getEndorsements(reid) + 1);
        SnappyStuff.k.updateEndorsed(seid,reid);
        EndorsementHandler.checkEndorsements(reid);
        c.getSource().sendSystemMessage(Component.literal("Endorsed " + praised.getName().getString() + "!"));
        return 0;



    }
}
