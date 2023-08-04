package com.cdogsnappy.snappystuff.quest.mission;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DailyReward{
    public Item reward;
    public int minPay;
    public int maxPay;
    public DailyReward(Item reward, int minPay, int maxPay){
        this.reward = reward;
        this.minPay = minPay;
        this.maxPay = maxPay;
    }
}