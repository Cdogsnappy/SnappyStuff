package com.cdogsnappy.snappystuff.quest.mission;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DailyMission extends Mission {
    public List<DailyReward> rewards;
    public Mission mission;
    public DailyMission(Mission mission, List<DailyReward> rewards){
        this.mission = mission;
        this.rewards = rewards;
    }
    /*
    public CompoundTag save(CompoundTag tag){
        tag.put("mission",mission.save(tag));
        tag.putInt("min",minPay);
        tag.putInt("max",maxPay);
        ListTag rew = new ListTag();
        rewards.forEach((s) -> {
            rew.add(s.save(new CompoundTag()));
        });
        tag.put("rewards",rew);
        return tag;
    }
    public static DailyMission load(CompoundTag tag){
        List<ItemStack> stacks = new ArrayList<>();
        ListTag rewards = (ListTag)tag.get("rewards");
        for(int j = 0; j<rewards.size(); ++j){
            stacks.add(ItemStack.of((CompoundTag) rewards.get(j)));
        }

        return new DailyMission(Mission.load(tag.getCompound("mission")),
            tag.getInt("min"),tag.getInt("max"), stacks);
    }

     */
}
