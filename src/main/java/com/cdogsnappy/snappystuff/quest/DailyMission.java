package com.cdogsnappy.snappystuff.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DailyMission extends Mission{

    int minPay;
    int maxPay;
    List<ItemStack> rewards;
    Mission mission;
    public DailyMission(Mission mission, int minPay, int maxPay, List<ItemStack> rewards){
        this.mission = mission;
        this.minPay = minPay;
        this.maxPay = maxPay;
    }
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
}
