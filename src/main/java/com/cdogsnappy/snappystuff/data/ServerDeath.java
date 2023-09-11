package com.cdogsnappy.snappystuff.data;
import com.cdogsnappy.snappystuff.karma.KarmaLog;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ServerDeath {

    /**
     * @author Cdogsnappy
     * Writes all KarmaLogs to a player instanced file and appends to them as the game progresses (handles all calculations)
     * @throws IOException
     */
    public static void writeKarmaLogs() throws IOException {
        Set<UUID> players = KarmaLog.karmaLogs.keySet();
        File dir = new File("karmalogs");
        if(!dir.exists()){
            dir.mkdir();
        }
        for(UUID id : players){
            KarmaLog log = KarmaLog.karmaLogs.get(id);
            File playerFile = new File("karmalogs/" + log.getPlayer() + ".txt");
            if(playerFile.exists()){
                /**
                 * list entries:
                 * 0 - Player Name (DO NOT ALTER)
                 * 1 - UUID (DO NOT ALTER)
                 * 2 - numDecreases (ADD TO PREVIOUS AMOUNT)
                 * 3 - numIncreases (ADD TO PREVIOUS AMOUNT)
                 * 4 - scores (APPEND TO END)
                 */
                List<String> list = Files.readAllLines(playerFile.toPath());
                if(log.getScores().size() > 1) {
                    log.getScores().remove(0);
                    String nextScores = log.getScores().toString();
                    list.set(2, list.get(2).substring(0, list.get(2).length() - 1) + ", " + nextScores.substring(1, nextScores.length()));
                }
                FileUtils.writeLines(playerFile,list,false);
            }
            else{//File doesn't exist, push all data to new file
                playerFile.createNewFile();
                List<String> list = new ArrayList<>();
                list.add(log.getPlayer());
                list.add(log.getPlayerID().toString());
                list.add(log.getScores().toString());
                FileUtils.writeLines(playerFile,list,false);

            }
            KarmaLog.karmaLogs.remove(id);
        }
    }

}
