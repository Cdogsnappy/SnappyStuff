package com.cdogsnappy.snappystuff.data;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.court.CourtCase;
import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.karma.KarmaPlayerInfo;
import com.cdogsnappy.snappystuff.radio.RadioHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.UUID;

public class ServerDeath {
    /**
     * @author bus7ed
     * Generates a file that stores all global persistent server data
     */
    public static void generateData(){
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
        }

    }
}
