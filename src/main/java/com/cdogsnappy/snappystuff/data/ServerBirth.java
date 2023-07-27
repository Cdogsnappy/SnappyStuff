package com.cdogsnappy.snappystuff.data;

import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.karma.KarmaPlayerInfo;
import com.cdogsnappy.snappystuff.radio.CustomSoundEvent;
import com.cdogsnappy.snappystuff.radio.RadioHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ServerBirth {

    /**
     * Reads all data stored from a previous server life
     */
    public static void readData(){
        HashMap<String, Object> serverData = new HashMap<String, Object>();
        File snappyData = new File("snappydata.txt");
        try {
            if (snappyData.exists()) {
                FileInputStream fos = new FileInputStream(snappyData);
                ObjectInputStream objReader = new ObjectInputStream(fos);
                serverData = (HashMap<String,Object>) objReader.readObject();

                if(serverData.containsKey("karma")){
                    Karma.karmaScores = (HashMap<UUID, KarmaPlayerInfo>) serverData.get("karma");
                }
                else{Karma.init();}
                if(serverData.containsKey("courts")){

                }
                if(serverData.containsKey("music")){
                    RadioHandler.music = (List<CustomSoundEvent>) serverData.get("music");
                }




                objReader.close();
            }
            else{
                Karma.init();
            }
        }
        catch(Exception e){

        }
    }

}
