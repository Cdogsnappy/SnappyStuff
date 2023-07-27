package com.cdogsnappy.snappystuff.court;

import net.minecraft.world.entity.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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

    public static void onPlayerJoin(Player p){
        if(!citizenRegistry.containsKey(p.getName().getString())){
            citizenRegistry.put(p.getName().getString(),new CitizenData(p));
        }
    }

}

