package com.cdogsnappy.snappystuff.blocks.entity;

import com.cdogsnappy.snappystuff.blocks.ModEntityBlocks;
import com.cdogsnappy.snappystuff.radio.CustomSoundEvent;
import com.cdogsnappy.snappystuff.radio.RadioHandler;
import com.cdogsnappy.snappystuff.radio.SoundInfo;
import com.cdogsnappy.snappystuff.screen.MusicUploadMenu;
import com.cdogsnappy.snappystuff.sounds.SSSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.Arrays;

public class MusicUploadBlockEntity extends BlockEntity implements MenuProvider {
    public int progress = 0;
    static final int completionProgress = 80;
    public boolean isProcessing = false;
    protected final ContainerData data;
    public final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public MusicUploadBlockEntity(BlockPos pos, BlockState state) {
        super(ModEntityBlocks.MUSIC_UPLOAD_BLOCK.get(), pos, state);
        data = new ContainerData(){
            @Override
            public int get(int num) {
                return switch(num){
                    case 0 -> MusicUploadBlockEntity.this.progress;

                    case 1 -> MusicUploadBlockEntity.this.completionProgress;
                    default -> 0;

                };
            }

            @Override
            public void set(int index, int val) {
                MusicUploadBlockEntity.this.progress = val;
            }


            @Override
            public int getCount() {
                return 2;
            }
        };

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Music Uploader");
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
        return new MusicUploadMenu(id, inv, this, this.data);
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }


    public static <E extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, MusicUploadBlockEntity e) {
        if(e.itemHandler.getStackInSlot(0).getItem() instanceof RecordItem){
            e.isProcessing = true;
        }
        else{
            return;
        }
        if(level.isClientSide || !e.isProcessing){
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
       SSSoundRegistry.uploadableSounds.add(song.getSound());
        RadioHandler.musicLocations.add(new SoundInfo(SSSoundRegistry.uploadableSounds.indexOf(song.getSound()),song.getLengthInTicks()));
        RadioHandler.music.add(new CustomSoundEvent(song.getSound(), song.getLengthInTicks()));
    }
}
