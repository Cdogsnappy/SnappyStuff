package com.cdogsnappy.snappymod.karma;

import com.cdogsnappy.snappymod.SnappyMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EndorsementHandler {
    public static int dailyEndorsements = 3;
    public static void checkEndorsements(UUID id){
        if(SnappyMod.k.getEndorsements(id)>=4){
            SnappyMod.k.setScore(id,SnappyMod.k.getScore(id) + 2);
            SnappyMod.k.setEndorsements(id,SnappyMod.k.getEndorsements(id) - 4);
        }
    }
    public static void updateCooldowns(MinecraftServer server){
        List<ServerPlayer> players =  server.getPlayerList().getPlayers();
        for (ServerPlayer p : players) {
            KarmaPlayerInfo info = SnappyMod.k.getKarmaInfo(p.getUUID());
            for(int i = 0; i<dailyEndorsements; i++){
                if(LocalDateTime.now().isAfter(info.playersEndorsed[i].time)){
                    info.playersEndorsed[i] = null;
                    info.numEndorsed--;
                }
        }
            SnappyMod.k.setKarmaInfo(p.getUUID(), info);
        }
    }

}
