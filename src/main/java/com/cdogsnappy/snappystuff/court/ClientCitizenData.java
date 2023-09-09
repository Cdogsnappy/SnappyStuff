package com.cdogsnappy.snappystuff.court;

import net.minecraft.nbt.CompoundTag;

import java.io.Serializable;
import java.util.UUID;

public class ClientCitizenData implements Serializable {
    public String name;
    public UUID playerID;

    public ClientCitizenData(String name, UUID id) {
        this.name = name;
        this.playerID = id;
    }

    public CompoundTag save(CompoundTag tag){
        tag.putString("name",name);
        tag.putUUID("id",playerID);
        return tag;
    }
    public static ClientCitizenData load(CompoundTag tag){return new ClientCitizenData(tag.getString("name"),tag.getUUID("id"));}

}
