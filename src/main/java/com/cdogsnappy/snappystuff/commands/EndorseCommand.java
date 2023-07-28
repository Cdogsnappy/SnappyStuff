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
import com.cdogsnappy.snappystuff.karma.Karma;

import java.util.Arrays;
import java.util.UUID;

import static net.minecraft.commands.arguments.EntityArgument.getPlayer;

public class EndorseCommand {
    public static void register(LiteralArgumentBuilder<CommandSourceStack> root) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("endorse").requires(c -> c.hasPermission(1));

        builder.then(
                Commands.argument("player", EntityArgument.player())
                        .executes(c -> {
                            return endorse(c, getPlayer(c,"player"), c.getSource().getPlayer());
                        })
        );
        root.then(builder);
    }

    /**
     * @author Cdogsnappy
     * @param c Context from endorse command
     * @param praised person being endorsed
     * @param praiser person endorsing
     * @return command success/fail value
     */
    public static int endorse(CommandContext<CommandSourceStack> c, Player praised, Player praiser){
        if(praised == praiser){
            c.getSource().sendFailure(Component.literal("You cannot endorse yourself!"));
            return -3;
        }
        UUID reid = praised.getUUID();//receiver
        UUID seid = praiser.getUUID();//sender
        if(Karma.getEndorsed(seid) == 3){//CHECK THAT PRAISER HAS ENDORSEMENTS LEFT
            c.getSource().sendFailure(Component.literal("You've already used all of your endorsements today!"));
            return -1;
        }
        EndorsementInfo[] e = Karma.getKarmaInfo(seid).getPlayersEndorsed();

        for(int i = 0; i<3; i++) {//CHECK THAT PRAISER HASN'T ALREADY ENDORSED THIS PLAYER
            if(e[i] == null){
                continue;
            }
            if (e[i].getID().equals(reid)) {// == doesn't work here :\, use .equals() is you know your obj1 is non-null
                c.getSource().sendFailure((Component.literal("You've already praised " + praised.getName().getString() + " today!")));
                return -2;
            }
        }
        Karma.setEndorsements(reid,Karma.getEndorsements(reid) + 1);
        Karma.updateEndorsed(seid,reid);
        EndorsementHandler.checkEndorsements(reid);
        c.getSource().sendSystemMessage(Component.literal("Endorsed " + praised.getName().getString() + "!"));
        return 0;



    }
}
