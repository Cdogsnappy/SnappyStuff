package com.cdogsnappy.snappystuff.karma;

import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EndorsementHandler {
    public static int dailyEndorsements = 3;
    public static void checkEndorsements(UUID id){
        if(Karma.getEndorsements(id)>=4){
            Karma.setScore(id,Karma.getScore(id) + 2);
            Karma.setEndorsements(id,Karma.getEndorsements(id) - 4);
        }
    }
    public static void updateCooldowns(MinecraftServer server){
        List<ServerPlayer> players =  server.getPlayerList().getPlayers();
        for (ServerPlayer p : players) {
            KarmaPlayerInfo info = Karma.getKarmaInfo(p.getUUID());
            for(int i = 0; i<dailyEndorsements; i++){
                if(LocalDateTime.now().isAfter(info.playersEndorsed[i].time)){
                    info.playersEndorsed[i] = null;
                    info.numEndorsed--;
                }
        }
            Karma.setKarmaInfo(p.getUUID(), info);
        }
    }

}
