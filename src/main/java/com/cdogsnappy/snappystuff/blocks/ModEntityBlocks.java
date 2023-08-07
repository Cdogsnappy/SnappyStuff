package com.cdogsnappy.snappystuff.blocks;

import com.cdogsnappy.snappystuff.blocks.entity.MusicUploadBlockEntity;
import com.cdogsnappy.snappystuff.blocks.entity.QuestAcceptBlockEntity;
import com.cdogsnappy.snappystuff.blocks.entity.QuestCreationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.cdogsnappy.snappystuff.SnappyStuff;

public class ModEntityBlocks {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SnappyStuff.MODID);

    public static final RegistryObject<BlockEntityType<MusicUploadBlockEntity>> MUSIC_UPLOAD_BLOCK =
            BLOCK_ENTITIES.register("music_upload", () ->
                    BlockEntityType.Builder.of(MusicUploadBlockEntity::new, ModBlocks.MUSIC_UPLOAD_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<QuestAcceptBlockEntity>> QUEST_ACCEPT_BLOCK =
            BLOCK_ENTITIES.register("quest_accept", () ->
                    BlockEntityType.Builder.of(QuestAcceptBlockEntity::new, ModBlocks.QUEST_ACCEPT_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<QuestCreationBlockEntity>> QUEST_CREATE_BLOCK =
            BLOCK_ENTITIES.register("quest_create", () ->
                BlockEntityType.Builder.of(QuestCreationBlockEntity::new, ModBlocks.QUEST_CREATE_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}