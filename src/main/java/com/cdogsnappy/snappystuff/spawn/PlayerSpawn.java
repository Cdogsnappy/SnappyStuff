package com.cdogsnappy.snappystuff.spawn;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PlayerSpawn{
    public static HashMap<UUID, BlockPos> playerSpawns = new HashMap<>();
    public static List<BlockPos> possibleSpawns = Lists.newArrayList();
    private static Random rand = new Random();

    public static CompoundTag save(CompoundTag tag) {
        ListTag playerData = new ListTag();
        ListTag spawnData = new ListTag();
        playerSpawns.forEach((player,pos) -> {
            CompoundTag playerInstance = new CompoundTag();
            playerInstance.putUUID("player",player);
            playerInstance.putInt("x",pos.getX());
            playerInstance.putInt("y",pos.getY());
            playerInstance.putInt("z",pos.getZ());
            playerData.add(playerInstance);
        });
        tag.put("playerList",playerData);
        possibleSpawns.forEach((pos) -> {
            CompoundTag spawnPoint = new CompoundTag();
            spawnPoint.putInt("x",pos.getX());
            spawnPoint.putInt("y",pos.getY());
            spawnPoint.putInt("z", pos.getZ());
            spawnData.add(spawnPoint);
        });
        tag.put("spawns",spawnData);
        return tag;
    }
    public static void load(CompoundTag tag){
        ListTag playerData = (ListTag)tag.get("playerList");
        for(int i = 0; i < playerData.size(); ++i){
            CompoundTag player = playerData.getCompound(i);
            UUID p = player.getUUID("player");
            int x = player.getInt("x");
            int y = player.getInt("y");
            int z = player.getInt("z");
            BlockPos blockPos = new BlockPos(x,y,z);
            playerSpawns.put(p,blockPos);
        }
        ListTag spawnData = (ListTag)tag.get("spawns");
        for(int j = 0; j < spawnData.size(); ++j){
            CompoundTag blockPos = (CompoundTag)spawnData.get(j);
            possibleSpawns.add(new BlockPos(blockPos.getInt("x"),blockPos.getInt("y"),blockPos.getInt("z")));
        }
    }

    public static void onPlayerJoin(Player player){
        if(possibleSpawns.size() == 0){
            return;//Can't do much if there are no custom spawns
        }
        if(!playerSpawns.containsKey(player.getUUID())){
            playerSpawns.put(player.getUUID(),possibleSpawns.get(rand.nextInt(possibleSpawns.size())));
        }
    }
    public static void onPlayerSpawn(ServerPlayer player){
        BlockPos vanillaSpawn = player.getLevel().getSharedSpawnPos();
        if(player.getRespawnPosition() == vanillaSpawn){
            if(checkOrAddCustomSpawn(player.getUUID())) {
                player.sendSystemMessage(Component.literal("Successful spawn check! " + playerSpawns.get(player.getUUID()).toString()));
                BlockPos pos = playerSpawns.get(player.getUUID());
                player.teleportTo(pos.getX(),pos.getY(),pos.getZ());
            }
        }
    }
    public static boolean checkOrAddCustomSpawn(UUID player){
        if(possibleSpawns.size() == 0){
            return false;//Nothing to bother with, can't assign a custom spawn
        }
        if(!playerSpawns.containsKey(player)){//If no entry, make one, otherwise we have done our job
            playerSpawns.put(player,possibleSpawns.get(rand.nextInt(possibleSpawns.size())));
        }
        return true;
    }
    public static void rerollSpawns(BlockPos pos){
        boolean hasOtherSpawns = possibleSpawns.size()>0;
        playerSpawns.forEach(((p,spawn) -> {
            if(spawn.compareTo(pos) == 0){
                if(hasOtherSpawns) {
                    playerSpawns.put(p, possibleSpawns.get(rand.nextInt(possibleSpawns.size())));
                }
                else{playerSpawns.remove(p);}//Simply remove if there are no alternative spawns
            }
        }));
    }
    public static int rerollSpawns(){
        if(possibleSpawns.size() == 0){return -1;}
        playerSpawns.forEach((p,spawn) -> {
            playerSpawns.put(p,possibleSpawns.get(rand.nextInt(possibleSpawns.size())));
        });
        return 0;
    }
    public static int rerollSpawns(UUID player){
        if(possibleSpawns.size() == 0){return -1;}
        playerSpawns.put(player,possibleSpawns.get(rand.nextInt(possibleSpawns.size())));
        return 0;
    }
}