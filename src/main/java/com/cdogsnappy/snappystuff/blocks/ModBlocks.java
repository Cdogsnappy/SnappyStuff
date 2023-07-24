package com.cdogsnappy.snappystuff.blocks;

import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.obj.ObjMaterialLibrary;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SnappyStuff.MODID);

    public static final RegistryObject<Block> MUSIC_UPLOAD_BLOCK = BLOCKS.register("music_upload_block", () -> new Block(BlockBehaviour.Properties.of()));
}
