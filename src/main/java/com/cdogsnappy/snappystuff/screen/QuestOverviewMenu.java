package com.cdogsnappy.snappystuff.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import java.util.List;

public class QuestOverviewMenu extends AbstractContainerMenu {
    List<RewardSlot> rewardSlots = Lists.newArrayList();

    private final ItemStackHandler itemHandler = new ItemStackHandler(65) {
        @Override
        protected void onContentsChanged (int slot) {
            updateRewardRegistry(this.getStackInSlot(slot),slot);
        }
    };
    public QuestOverviewMenu(int id, Inventory inv) {
        super(ModMenus.QUEST_ACCEPT_MENU.get(), id);
        addPlayerHotbar(inv);
        addPlayerInventory(inv);
        addSlots();
    }

    private void addSlots(){
        for(int j = 0; j < 6; ++j){
            for(int i = 0; i < 10; ++i){
                rewardSlots.add((RewardSlot) this.addSlot(new RewardSlot(itemHandler,i,39 + i*18, 19 + j*18)));
            }
        }
        this.addSlot(new DisplaySlot(itemHandler, 60, 210, 29));
        this.addSlot(new DisplaySlot(itemHandler,61,210,47));
        this.addSlot(new DisplaySlot(itemHandler,62,210,65));
        this.addSlot(new DisplaySlot(itemHandler,63,210,83));
        this.addSlot(new DisplaySlot(itemHandler,64,210,101));
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 5;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return false;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 24 + l * 18, 141 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 24 + i * 18, 199));
        }
    }
    private void updateRewardRegistry(ItemStack stack, int slot){

    }
    protected void changeRewardVisibility(boolean vis){
        if(rewardSlots.get(0).isActive() != vis){return;}//rewards slots are binary, all will be either on or off
        rewardSlots.forEach((s) -> {
            s.setActive(vis);
        });
    }
}