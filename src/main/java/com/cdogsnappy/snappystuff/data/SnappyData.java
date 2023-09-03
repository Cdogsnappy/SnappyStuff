package com.cdogsnappy.snappystuff.data;

import com.cdogsnappy.snappystuff.quest.QuestHandler;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.cdogsnappy.snappystuff.spawn.PlayerSpawn;
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
        tag.put("SpawnData", PlayerSpawn.save(new CompoundTag()));
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
        PlayerSpawn.load(tag.getCompound("SpawnData"));
        return new SnappyData();
    }
    public static void setDataDirty(){
        snappyData.setDirty();
    }
}
