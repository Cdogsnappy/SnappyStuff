package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * Some clientside data that gets modified when a client requests data from the server, THIS IS NEVER ACCESSED SERVERSIDE, IT IS USELESS THERE
 */
public class QuestScreensData {
    public static List<String> filteredTokens = new ArrayList<>();//Player will only ever be looking at one menu, block menu or entity menu, so only need one filtered list.
    public static List<String> entitySearchTokens = new ArrayList<>();
    public static List<String> blockSearchTokens = new ArrayList<>();
    public static List<String> playerSearchTokens = new ArrayList<>();
    public static Quest questAcceptScreenDisplay = null;
    public static List<ClosedContractQuest> playerQuests = null;//Player instanced quests, sent all in one packet so as not to require a packet every time the player move to the next quest.
    public static boolean playerQuestsStale = true;//If the player has accepted or completed quests at any point, the client will mark this true so that it knows to request the player quests again.
    public static List<ItemStack> playerRewards = new ArrayList<>();

    /**
     * For rendering purposes
     */
    public static void init(){
        ForgeRegistries.ENTITY_TYPES.getValues().stream().forEach((e) -> {
            entitySearchTokens.add(e.getDescription().getString() + "," + ForgeRegistries.ENTITY_TYPES.getKey(e).toString());
        });
        ForgeRegistries.BLOCKS.getValues().stream().forEach((b) -> {
            blockSearchTokens.add(b.getName().getString() + "," + ForgeRegistries.BLOCKS.getKey(b));
        });




    }

    /**
     * @author Cdogsnappy
     * Called when a player changes the text in the search bar, refrehes the list of displayed objects
     * @param token the string that the player is searching for
     * @param toFilter marker letting us know which list we are searching in.
     */
    public static void refreshList(String token, int toFilter){
        filteredTokens = new ArrayList<>();
        switch(toFilter){
            case 0:
                entitySearchTokens.forEach((s) ->{
                    if(s.contains(token)){
                        filteredTokens.add(s);
                    }
                });
            case 1:
                blockSearchTokens.forEach((s) ->{
                    if(s.contains(token)){
                        filteredTokens.add(s);
                    }
                });
            case 2:
                playerSearchTokens.forEach((s) -> {
                    if(s.contains(token)){
                        filteredTokens.add(s);
                    }
                });


        }

    }
}
