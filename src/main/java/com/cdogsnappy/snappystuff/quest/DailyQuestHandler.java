package com.cdogsnappy.snappystuff.quest;

import com.cdogsnappy.snappystuff.quest.mission.DailyMission;
import com.cdogsnappy.snappystuff.quest.mission.DailyReward;
import com.cdogsnappy.snappystuff.quest.mission.Mission;
import com.cdogsnappy.snappystuff.quest.mission.MissionHandler;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyQuestHandler {

    static int numberDailies = 10;
    static Random rand = new Random();

    public static LocalDateTime lastRefresh = LocalDateTime.now().minusDays(2);//If this isn't overwritten on server start then no dailies are active.

    /**
     * Run every 30 minutes(very small optimization over running it less often)
     */
    public static void checkRefresh(){
        if(LocalDateTime.now().isBefore(lastRefresh.plusDays(1))){
            return;
        }
        QuestHandler.unacceptedQuests.forEach((q) -> {//REMOVE ALL UNACCEPTED DAILY QUESTS
            if(q.type == Quest.QuestType.DAILY){
                QuestHandler.unacceptedQuests.remove(q);
            }
        });

        List<ClosedContractQuest> newDailyQuests = generateNewDailyQuests(numberDailies);
        if(newDailyQuests.isEmpty()){
            return;
        }
        QuestHandler.unacceptedQuests.addAll(newDailyQuests);

    }

    public static List<ClosedContractQuest> generateNewDailyQuests(int num){
        int numMissionsAvailable;
        if((numMissionsAvailable = MissionHandler.dailyMissionList.size()) <= 5){
            return new ArrayList<>();
        }
        List<ClosedContractQuest> dailyQuests = new ArrayList<>();
        for(int j = 0; j<num; ++j){
            List<DailyMission> missions = new ArrayList<>();
            int numMissions = rand.nextInt(3)+1;
            for(int i = 0; i<numMissions;++i){
                missions.add(MissionHandler.dailyMissionList.get(rand.nextInt(numMissionsAvailable)));
            }
            List<Mission> missionList = new ArrayList<>();
            missions.forEach((m) -> {
                missionList.add(m.mission);
            });
            List<ItemStack> rewards = coalesceRewards(missions);
            dailyQuests.add(new ClosedContractQuest(missionList,rewards,Quest.radiantID, Quest.QuestType.DAILY));
        }
        return dailyQuests;
    }
    /**
     * @author Cdogsnappy
     * Takes the rewards of all the missions and coalesces all the guarenteed and then as many lower priority rewards as possible and determines the number of each item to be given as a reward.
     * @param missions the list of missions for this quest.
     * @return the list of coalesced rewards
     */
    public static List<ItemStack> coalesceRewards(List<DailyMission> missions){
        List<ItemStack> res = new ArrayList<>();
        List<DailyReward> rewards = new ArrayList<>();
        missions.forEach((m) -> {
            rewards.addAll(m.rewards);
        });
        /**
         * This big double for loop will guarantee the top priority reward from each mission is included in the quest rewards.
         */
        missions.forEach((m) -> {
            final int[] min = new int[1];
            final int[] max = new int[1];
            Item i = m.rewards.remove(0).reward;
            rewards.forEach((r) -> {
                if(r.reward == i){
                    min[0] = min[0] + r.minPay;
                    max[0] = max[0] + r.maxPay;
                    rewards.remove(r);
                }
            });
            res.add(new ItemStack(i,rand.nextInt(max[0]-min[0]) + min[0] + 1));
        });
        /**
         * This while loop will randomly fill the last rewards from the remaining reward pool of the missions or, if there is less than 5 unique rewards,
         * generate the quest with all of the mission rewards.
         */
        while((!rewards.isEmpty()) && res.size() < 5){
            DailyMission dM;
            if((dM = missions.get(rand.nextInt(missions.size()))).rewards.isEmpty())
            {
                missions.remove(dM);
                continue;
            }
            Item i = dM.rewards.remove(0).reward;
            final int[] min = new int[1];
            final int[] max = new int[1];
            rewards.forEach((r) ->{
                if(r.reward == i){
                    min[0] = min[0] + r.minPay;
                    max[0] = max[0] + r.maxPay;
                    rewards.remove(r);
                }
            });
            res.add(new ItemStack(i,rand.nextInt(max[0]-min[0]) + min[0] + 1));
        }
        return res;
    }

}
