package com.cdogsnappy.snappystuff.data;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.karma.Karma;
import com.cdogsnappy.snappystuff.quest.DailyQuestHandler;
import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class SnappyData extends SavedData {

    public static SnappyData snappyData;
    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("QuestData", QuestHandler.save(new CompoundTag()));
        tag.put("RadioData", RadioHandler.save(new CompoundTag()));
        tag.put("KarmaData", Karma.save(new CompoundTag()));
        tag.put("CitizenData", CitizenData.saveRegistry(new CompoundTag()));
        tag.put("DailyTime", DailyQuestHandler.saveTime(new CompoundTag()));
        return tag;
    }

    public static SnappyData get(Level level){
        if(level.isClientSide){
            throw new RuntimeException("CANNOT SAVE GLOBAL DATA ON CLIENT SIDE");
        }
        DimensionDataStorage dataStorage = ((ServerLevel)level).getDataStorage();
        return snappyData =  dataStorage.computeIfAbsent(SnappyData::load,SnappyData::new,"snappydata");
    }
    public SnappyData(){snappyData=this;}
    public static SnappyData load(CompoundTag tag){
        QuestHandler.load(tag.getCompound("QuestData"));
        RadioHandler.load(tag.getCompound("RadioData"));
        Karma.load(tag.getCompound("KarmaData"));
        CitizenData.loadRegistry(tag.getCompound("CitizenData"));
        DailyQuestHandler.loadTime(tag.getCompound("DailyTime"));
        return new SnappyData();
    }
    public static void setDataDirty(){
        snappyData.setDirty();
    }
}
