package com.cdogsnappy.snappystuff.data;

import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.karma.KarmaPlayerInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ServerBirth {

    /**
     * Reads all data stored from a previous server life
     */
    public static void readData(){
        HashMap<String, Object> serverData = new HashMap<String, Object>();
        File karma = new File("karma.txt");
        try {
            if (snappyData.exists()) {
                FileInputStream fos = new FileInputStream(karma);
                ObjectInputStream objReader = new ObjectInputStream(fos);
                Karma.karmaScores = (HashMap<UUID, KarmaPlayerInfo>) objReader.readObject();
                objReader.close();
                LOGGER.info("SUCCESSFULLY RESTORED SNAPPYSTUFF DATA");
            }
            else{
                Karma.init();
            }
        }
        catch(Exception e){
            LOGGER.info("FATAL ERROR RELOADING DATA");
        }
    }

}
