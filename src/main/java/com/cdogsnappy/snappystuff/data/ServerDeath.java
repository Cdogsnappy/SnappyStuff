package com.cdogsnappy.snappystuff.data;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.court.CourtCase;
import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.karma.KarmaLog;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.*;

public class ServerDeath {
    /**
     * @author bus7ed
     * Generates a file that stores all global persistent server data
     */
    public static void generateData(Logger logger){
        try {
            File snappyData = new File("snappydata.dat");
            if (snappyData.exists()) {
                snappyData.delete();
            }
            snappyData.createNewFile();
            FileOutputStream fos = new FileOutputStream(snappyData);
            ObjectOutputStream objWriter = new ObjectOutputStream(fos);
            HashMap<String,Object> serverData = new HashMap<String,Object>();//FILL THIS HASHMAP WITH THE INFO WE WANT TO SAVE
            serverData.put("karma", Karma.karmaScores);
            serverData.put("citizens", CitizenData.citizenRegistry);
            serverData.put("cases", CourtCase.caseArchive);
            serverData.put("music", RadioHandler.musicLocations);
            objWriter.writeObject(serverData);
            objWriter.flush();
            objWriter.close();
        }
        catch(IOException e){
            logger.info("FATAL ERROR SAVING SNAPPY DATA");
        }

    }

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
                log.getScores().remove(0);
                String nextScores = log.getScores().toString();
                list.set(2,list.get(2).substring(0,list.get(2).length()-1) + ", " + nextScores.substring(1,nextScores.length()));
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
