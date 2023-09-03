package com.cdogsnappy.snappystuff.quest.mission;

import com.cdogsnappy.snappystuff.quest.mission.DailyMission;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.openjdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MissionJSONHandler {
    private static final File file = new File("snappystuff/missions.json");

    public static void init() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            JsonWriter writer = new JsonWriter(new FileWriter(file,true));
            writer.beginArray();
            writer.endArray();
            writer.flush();
            writer.close();
        }
    }
    public static void writeMission(DailyMission mission) throws IOException {
        JsonArray el = JsonParser.parseReader(new FileReader(file)).getAsJsonArray();
        el.add(serialize(mission));
        file.delete();
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(el.toString());
        writer.flush();
        writer.close();
    }
    public static void readDailies() throws FileNotFoundException {
        JsonArray el = JsonParser.parseReader(new FileReader(file)).getAsJsonArray();
        el.forEach((o) -> {
            JsonObject obj = o.getAsJsonObject();
            MissionHandler.dailyMissionList.add(deserialize(obj));
        });
    }
        public static JsonElement serialize(DailyMission src) {
            JsonObject res = new JsonObject();
            res.addProperty("type", src.mission.missionType.toString());
            switch (src.mission.missionType) {
                case KILL:
                    res.addProperty("target", ForgeRegistries.ENTITY_TYPES.getKey(((KillMission) src.mission).toKill).toString());
                    res.addProperty("amount", ((KillMission) src.mission).numToKill);
                    break;
                case BLOCK:
                    res.addProperty("target", ForgeRegistries.BLOCKS.getKey(((BlockMission) src.mission).toBreak).toString());
                    res.addProperty("amount", ((BlockMission) src.mission).numToBreak);
                    break;
                case COLLECT:
                    res.addProperty("target", ForgeRegistries.ITEMS.getKey(((CollectMission) src.mission).toCollect.getItem()).toString());
                    res.addProperty("amount", ((CollectMission) src.mission).toCollect.getCount());
                    break;
                default:
                    break;

            }
            JsonArray rewards = new JsonArray();
            src.rewards.forEach((i) -> {
                JsonObject rewObj = new JsonObject();
                rewObj.addProperty("item", ForgeRegistries.ITEMS.getKey(i.reward).toString());
                rewObj.addProperty("minPay", i.minPay);
                rewObj.addProperty("maxPay", i.maxPay);
                rewards.add(rewObj);
            });
            res.add("rewards", rewards);
            return res;
        }
        public static DailyMission deserialize(JsonElement json) throws JsonParseException {
            List<DailyReward> rewardList = new ArrayList<>();
            JsonObject obj = json.getAsJsonObject();
            JsonArray rewardsJson = obj.get("rewards").getAsJsonArray();
            rewardsJson.forEach((s) -> {
                JsonObject rewObj = s.getAsJsonObject();
                rewardList.add(new DailyReward(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(rewObj.get("item").getAsString())),rewObj.get("minPay").getAsInt(),rewObj.get("maxPay").getAsInt()));
            });
            String target = obj.get("target").getAsString();
            int count = obj.get("count").getAsInt();
            Mission mission;
            switch(obj.get("type").getAsString()){
                case "KILL":
                    mission = new KillMission(ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(target)),count);
                    break;
                case "BLOCK":
                    mission = new BlockMission(ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(target)),count);
                    break;
                case "COLLECT":
                    mission = new CollectMission(new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(target)),count));
                    break;
                default:
                    mission = null;

            }
            return new DailyMission(mission,rewardList);
        }
    }