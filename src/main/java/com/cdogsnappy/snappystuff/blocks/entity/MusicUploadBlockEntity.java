package com.cdogsnappy.snappystuff.blocks.entity;

import com.cdogsnappy.snappystuff.blocks.ModEntityBlocks;
import com.cdogsnappy.snappystuff.radio.CustomSoundEvent;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.cdogsnappy.snappystuff.screen.MusicUploadMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class MusicUploadBlockEntity extends BlockEntity implements MenuProvider {
    public int progress = 0;
    static final int completionProgress = 80;
    public boolean isProcessing = false;
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public MusicUploadBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntityBlocks.MUSIC_UPLOAD_BLOCK.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return null;
    }
    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("progress", this.progress);
        super.saveAdditional(tag);

    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new MusicUploadMenu(id, inv, this, this.progress);
    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, MusicUploadBlockEntity e) {
        if(level.isClientSide || !e.isProcessing){
            return;
        }
        if(!(e.itemHandler.getStackInSlot(0).getItem() instanceof RecordItem)){
            e.isProcessing = false;
            return;
        }
        e.progress++;
        if(e.progress >= completionProgress){
            uploadSong((RecordItem) e.itemHandler.getStackInSlot(0).getItem());
        }

    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }
    private static void uploadSong(RecordItem song){
        for(CustomSoundEvent s : RadioHandler.music){
            if(s.getSound().equals(song.getSound())){
                return;
            }
        }
        RadioHandler.music.add(new CustomSoundEvent(song.getSound(), song.getLengthInTicks()));
    }
}
