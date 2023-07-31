package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestHandler extends SavedData {
    public static List<Quest> quests = new ArrayList<>();
    public static QuestHandler q;

    public static QuestHandler get(Level level){
        if(level.isClientSide){
            throw new RuntimeException("CANNOT SAVE GLOBAL DATA ON CLIENT SIDE");
        }
        DimensionDataStorage dataStorage = ((ServerLevel)level).getDataStorage();
        return q =  dataStorage.computeIfAbsent(QuestHandler::load,QuestHandler::new,"questhandler");

    }

    public QuestHandler(){

    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag questsTag = new ListTag();
        for(int j = 0; 0 < quests.size(); ++j){
            questsTag.add(Quest.save(new ListTag(),quests.get(j)));
        }
        tag.put("quests",questsTag);
        return tag;
    }

    public static QuestHandler load(CompoundTag tag){
        ListTag questsTag = (ListTag)tag.get("quests");
        for(int i = 0; i<questsTag.size(); i++){
            CompoundTag quest = questsTag.getCompound(i);
            Quest.load(quest);
        }
        return new QuestHandler();
    }
    public static void setQuestsDirty(){
        q.setDirty();
    }

}
