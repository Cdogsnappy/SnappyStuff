package com.cdogsnappy.snappystuff.court;

import com.cdogsnappy.snappystuff.network.AvailablePlayersPacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.compress.utils.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Cdogsnappy
 * class to store all instanced data about a given player, citizenRegistry holds data of all players
 */
public class CitizenData implements Serializable {
    public static HashMap<String, CitizenData> citizenRegistry = new HashMap<String, CitizenData>();
    public static List<ClientCitizenData> citizenNames = new ArrayList<>();

    protected UUID id;
    protected String name;
    protected int[] services = new int[5];//Map, 0 = Judge, 1 = Jury, 2 = Defendant, 3 = Plaintiff, 4 = Lawyer

    public CitizenData(Player p){
        id = p.getUUID();
        name = p.getName().getString();
    }
    public CitizenData(UUID id, String name, int[] services){
        this.id = id;
        this.name = name;
        this.services = services;
    }

    public String getName(){return name;}
    public UUID getUUID(){return id;}
    public int[] getServices(){return services;}

    /**
     * @author Cdogsnappy
     * Checks if a player is in the citizen registry when they join
     * @param p Player to check
     */

    public static void onPlayerJoin(Player p){
        if(!citizenRegistry.containsKey(p.getName().getString())){
            citizenRegistry.put(p.getName().getString(),new CitizenData(p));
            citizenNames.add(new ClientCitizenData(p.getScoreboardName(),p.getUUID()));
            SnappyNetwork.sendToAllPlayers(new AvailablePlayersPacket(citizenNames));
        }
    }
    public static String getPlayerName(UUID id){
        for(ClientCitizenData c : citizenNames){
            if(id==c.playerID){return c.name;}
        }
        return "";
    }

    public CompoundTag save(CompoundTag tag){
        tag.putUUID("id",id);
        tag.putString("name",name);
        tag.putIntArray("services",services);
        tag.put("crimes",crimeTag);
        return tag;
    }
    public static CitizenData load(CompoundTag tag){
        return new CitizenData(tag.getUUID("id"),tag.getString("name"),tag.getIntArray("services"));
    }

    public static CompoundTag saveRegistry(CompoundTag tag){
        ListTag citizenRegistryTag = new ListTag();
        citizenRegistry.forEach((name,cd) -> {
            CompoundTag citizen = new CompoundTag();
            citizen.putString("name",name);
            citizen.put("data",cd.save(new CompoundTag()));
        });
        tag.put("citizenRegistry",citizenRegistryTag);
        return tag;
    }
    public static void loadRegistry(CompoundTag tag){
        ListTag citizenRegistryTag = (ListTag)tag.get("citizenRegistry");
        for(int i = 0; i < citizenRegistryTag.size(); ++i){
            citizenRegistry.put(citizenRegistryTag.getCompound(i).getString("name"),CitizenData.load(citizenRegistryTag.getCompound(i).getCompound("data")));
        }
    }

}

