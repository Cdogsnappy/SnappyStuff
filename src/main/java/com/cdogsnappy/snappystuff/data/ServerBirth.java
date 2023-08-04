package com.cdogsnappy.snappystuff.data;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.court.CourtCase;
import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.karma.KarmaPlayerInfo;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.radio.CustomSoundEvent;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.cdogsnappy.snappystuff.radio.SoundInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ServerBirth {

    /**
     * @author Cdogsnappy
     * Reads all data stored from a previous server life
     */
    public static void readData(){
        HashMap<String, Object> serverData = new HashMap<String, Object>();
        File snappyData = new File("snappystuff/snappydata.dat");
        File snappyDir = new File("snappystuff");
        if(!snappyDir.exists()){
            snappyDir.mkdir();
        }
        try {
            if (snappyData.exists()) {
                FileInputStream fos = new FileInputStream(snappyData);
                ObjectInputStream objReader = new ObjectInputStream(fos);
                serverData = (HashMap<String,Object>) objReader.readObject();

                if(serverData.containsKey("karma")){
                    Karma.karmaScores = (HashMap<UUID, KarmaPlayerInfo>) serverData.get("karma");
                }
                else{Karma.init();}
                if(serverData.containsKey("citizens")){
                    CitizenData.citizenRegistry =(HashMap<String,CitizenData>)serverData.get("citizens");
                }
                if(serverData.containsKey("cases")){
                    CourtCase.caseArchive = (HashMap<Integer, CourtCase>)serverData.get("cases");
                }
                if(serverData.containsKey("music")){
                    RadioHandler.musicLocations = (List<SoundInfo>) serverData.get("music");
                }
                if(serverData.containsKey("dailytime")){
                    QuestHandler.dailiesTime = (LocalDateTime) serverData.get("dailytime");
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
