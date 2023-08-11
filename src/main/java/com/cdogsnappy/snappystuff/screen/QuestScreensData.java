package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.court.CitizenData;
import com.cdogsnappy.snappystuff.quest.ClosedContractQuest;
import com.cdogsnappy.snappystuff.quest.Quest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

/**
 * Some clientside data that gets modified when a client requests data from the server, THIS IS NEVER ACCESSED SERVERSIDE, IT IS USELESS THERE
 */
public class QuestScreensData {
    public enum ButtonType{
        BLOCK,
        COLLECT,
        KILL,
        PLAYERKILL
    }
    public static List<Object> filteredTokens = new ArrayList<>();//Player will only ever be looking at one menu, block menu or entity menu, so only need one filtered list.
    public static List<EntityType<?>> entitySearchTokens = new ArrayList<>();
    public static List<Block> blockSearchTokens = new ArrayList<>();
    public static List<String> playerSearchTokens = new ArrayList<>();
    public static List<Item> itemSearchTokens = new ArrayList<>();
    public static Quest questAcceptScreenDisplay = null;
    public static List<ClosedContractQuest> playerQuests = null;//Player instanced quests, sent all in one packet so as not to require a packet every time the player move to the next quest.
    public static boolean playerQuestsStale = true;//If the player has accepted or completed quests at any point, the client will mark this true so that it knows to request the player quests again.
    public static List<ItemStack> playerRewards = new ArrayList<>();

    /**
     * For rendering purposes
     */
    public static void init(){
        ForgeRegistries.ENTITY_TYPES.getValues().stream().forEach((e) -> {
            entitySearchTokens.add(e);
        });
        ForgeRegistries.BLOCKS.getValues().stream().forEach((b) -> {
            blockSearchTokens.add(b);
        });
        ForgeRegistries.ITEMS.getValues().stream().forEach((i) -> {
            itemSearchTokens.add(i);
        });




    }

    /**
     * @author Cdogsnappy
     * Called when a player changes the text in the search bar, refrehes the list of displayed objects
     * @param token the string that the player is searching for
     * @param type marker letting us know which list we are searching in.
     */
    public static void refreshList(String token, ButtonType type){
        filteredTokens = new ArrayList<>();
        switch(type){
            case KILL:
                if(token == ""){
                    filteredTokens.addAll(entitySearchTokens);
                    return;
                }
                entitySearchTokens.forEach((s) ->{
                    if(s.getCategory().getName().contains(token) || s.getDescription().getString().contains(token)){
                        filteredTokens.add(s);
                    }
                });
            case BLOCK:
                if(token == ""){
                    filteredTokens.addAll(blockSearchTokens);
                    return;
                }
                blockSearchTokens.forEach((s) ->{
                    if(s.getName().getString().contains(token)){
                        filteredTokens.add(s);
                    }
                });
            case PLAYERKILL:
                if(token == ""){
                    filteredTokens.addAll(playerSearchTokens);
                    return;
                }
                playerSearchTokens.forEach((s) -> {
                    if(s.contains(token)){
                        filteredTokens.add(s);
                    }
                });
            case COLLECT:
                if(token == ""){
                    filteredTokens.addAll(itemSearchTokens);
                }
                itemSearchTokens.forEach((i) -> {
                    if(i.getDescription().getString().contains(token)){
                        filteredTokens.add(i);
                    }
                });
        }

    }
}
