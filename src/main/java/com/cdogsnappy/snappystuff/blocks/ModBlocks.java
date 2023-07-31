package com.cdogsnappy.snappystuff.blocks;

import com.cdogsnappy.snappystuff.SnappyStuff;
import com.cdogsnappy.snappystuff.items.ModItems;
import com.cdogsnappy.snappystuff.items.SnappyStuffTabs;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.obj.ObjMaterialLibrary;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SnappyStuff.MODID);

    public static final RegistryObject<Block> MUSIC_UPLOAD_BLOCK = registerBlock("music_upload_block", () -> new MusicUploadBlock(BlockBehaviour.Properties.of(Material.STONE)), SnappyStuffTabs.SNAPPY_STUFF_TAB);

    public static final RegistryObject<Block> QUEST_ACCEPT_BLOCK = registerBlock("quest_accept_block", () -> new QuestAcceptBlock(BlockBehaviour.Properties.of(Material.STONE)), SnappyStuffTabs.SNAPPY_STUFF_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}
