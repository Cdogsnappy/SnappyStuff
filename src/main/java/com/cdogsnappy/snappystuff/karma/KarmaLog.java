package com.cdogsnappy.snappystuff.karma;

import net.minecraft.world.entity.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Player instanced log for how their karma changes throughout the game
 */
public class KarmaLog {
    public static HashMap<UUID,KarmaLog> karmaLogs = new HashMap<>();

    protected String player;
    protected UUID playerID;
    protected ArrayList<Float> scores;
    protected float currentScore;

    public KarmaLog(Player player){
        this.player = player.getName().getString();
        this.playerID = player.getUUID();
        scores = new ArrayList<>();
        this.currentScore = Karma.getScore(player.getUUID());
        scores.add(currentScore);

    }
    public String getPlayer(){return player;}
    public UUID getPlayerID(){return playerID;}
    public ArrayList<Float> getScores(){return scores;}



    public static void update(UUID id){
        KarmaLog log = karmaLogs.get(id);
        log.scores.add(Karma.getScore(id));
        karmaLogs.put(id,log);
    }
    public static void onPlayerJoin(Player player){
        if(!karmaLogs.containsKey(player.getUUID())) {
            karmaLogs.put(player.getUUID(), new KarmaLog(player));
        }
    }
}
