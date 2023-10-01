package com.cdogsnappy.snappystuff.screen;

import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;


public class QuestOverviewMenu extends AbstractContainerMenu {

    private final ItemStackHandler itemHandler = new ItemStackHandler(65) {
        @Override
        protected void onContentsChanged (int slot) {}
    };
    public QuestOverviewMenu(int id, Inventory inv, FriendlyByteBuf friendlyByteBuf) {
        super(ModMenus.QUEST_OVERVIEW_MENU.get(),id);
        addPlayerHotbar(inv);
        addPlayerInventory(inv);
        addDisplaySlots();
    }
    public QuestOverviewMenu(int id, Inventory inv) {
        this(id,inv,null);
    }


    protected void removeRewardSlots(){
        for(int i = 0; i < 60; ++i){
            this.slots.remove(36);
        }
    }
    protected void removeDisplaySlots(){
        for(int j = 0; j < 5; ++j){
            this.slots.remove(36);
        }
    }
    protected void checkOrAddRewardSlots(){
        if(!(this.slots.get(36) instanceof RewardSlot)){
            removeDisplaySlots();
            for(int j = 0; j < 6; ++j){
                for(int i = 0; i < 10; ++i){
                    this.addSlot(new RewardSlot(itemHandler,i + j*10,39 + i*18, 19 + j*18));
                }
            }
        }
    }
    protected void checkOrAddDisplaySlots(){
        if(!(this.slots.get(36) instanceof DisplaySlot)){
            removeRewardSlots();
            addDisplaySlots();
        }
    }
    protected void addDisplaySlots(){
        this.addSlot(new DisplaySlot(itemHandler, 60, 210, 29));
        this.addSlot(new DisplaySlot(itemHandler,61,210,47));
        this.addSlot(new DisplaySlot(itemHandler,62,210,65));
        this.addSlot(new DisplaySlot(itemHandler,63,210,83));
        this.addSlot(new DisplaySlot(itemHandler,64,210,101));
    }
    public class RewardSlot extends SlotItemHandler {

        public RewardSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
        @Override
        public boolean mayPlace(ItemStack pStack) {
            return false;
        }
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
    public boolean stillValid(Player pPlayer) {
        return pPlayer.isAlive();
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 24 + l * 18, 141 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 24 + i * 18, 199));
        }
    }
    protected void clearRewardSlots(){
        for(int i = 0; i < 60; ++i){
            this.slots.get(36+i).set(ItemStack.EMPTY);
        }
    }

}