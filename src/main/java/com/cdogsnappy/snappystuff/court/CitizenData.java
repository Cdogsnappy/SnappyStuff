package com.cdogsnappy.snappystuff.court;

import com.cdogsnappy.snappystuff.network.AvailablePlayersPacket;
import com.cdogsnappy.snappystuff.network.SnappyNetwork;
import net.minecraft.world.entity.player.Player;

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
    public static List<String> citizenNames = new ArrayList<>();

    protected UUID id;
    protected String name;
    protected int[] services = new int[5];//Map, 0 = Judge, 1 = Jury, 2 = Defendant, 3 = Plaintiff, 4 = Lawyer
    protected ArrayList<Crime> crimes = new ArrayList<Crime>();

    public CitizenData(Player p){
        id = p.getUUID();
        name = p.getName().getString();
    }

    public String getName(){return name;}
    public UUID getUUID(){return id;}
    public int[] getServices(){return services;}
    public ArrayList<Crime> getCrimes(){return crimes;}

    /**
     * @author Cdogsnappy
     * Checks if a player is in the citizen registry when they join
     * @param p Player to check
     */

    public static void onPlayerJoin(Player p){
        if(!citizenRegistry.containsKey(p.getName().getString())){
            citizenRegistry.put(p.getName().getString(),new CitizenData(p));
            citizenNames.add(p.getName().getString());
            SnappyNetwork.sendToAllPlayers(new AvailablePlayersPacket(citizenNames));
        }
    }


}

