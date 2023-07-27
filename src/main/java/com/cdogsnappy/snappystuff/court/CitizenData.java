package com.cdogsnappy.snappystuff.court;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Cdogsnappy
 * class to store all instanced data about a given player, citizenRegistry holds data of all players
 */
public class CitizenData implements Serializable {
    public static HashMap<String, CitizenData> citizenRegistry = new HashMap<String, CitizenData>();

    protected UUID id;
    protected String name;
    protected int[] services = new int[4];
    protected ArrayList<Crime> crimes = new ArrayList<Crime>();

    public CitizenData(Player p){
        id = p.getUUID();
        name = p.getName().getString();
    }

    /**
     * @author Cdogsnappy
     * Checks if a player is in the citizen registry when they join
     * @param p Player to check
     */

    public static void onPlayerJoin(Player p){
        if(!citizenRegistry.containsKey(p.getName().getString())){
            citizenRegistry.put(p.getName().getString(),new CitizenData(p));
        }
    }


}

