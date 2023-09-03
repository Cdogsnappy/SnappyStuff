package com.cdogsnappy.snappystuff.screen;

import com.cdogsnappy.snappystuff.SnappyStuff;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, SnappyStuff.MODID);

    public static final RegistryObject<MenuType<MusicUploadMenu>> MUSIC_UPLOAD_MENU =
            registerMenuType(MusicUploadMenu::new, "music_upload_menu");

    public static final RegistryObject<MenuType<QuestAcceptMenu>> QUEST_ACCEPT_MENU =
            registerMenuType(QuestAcceptMenu::new, "quest_accept_menu");
    public static final RegistryObject<MenuType<QuestCreateMenu>> QUEST_CREATE_MENU =
            registerMenuType(QuestCreateMenu::new, "quest_create_menu");
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}