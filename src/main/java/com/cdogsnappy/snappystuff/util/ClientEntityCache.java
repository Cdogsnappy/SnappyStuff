package com.cdogsnappy.snappystuff.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * CREDIT: Shadows-of-Fire
 * https://github.com/Shadows-of-Fire/Hostile-Neural-Networks/blob/1.19/src/main/java/shadows/hostilenetworks/util/ClientEntityCache.java
 */
public class ClientEntityCache {
    private static final Map<Level, Map<EntityType<?>, LivingEntity>> CACHE = new WeakHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends LivingEntity> T computeIfAbsent(EntityType<T> type, Level level) {
        var map = CACHE.computeIfAbsent(level, l -> new HashMap<>());
        return (T) map.computeIfAbsent(type, k -> {
            T t = type.create(level);
            return t;
        });
    }

    public static void tick() {
        CACHE.values().stream().map(Map::values).flatMap(Collection::stream).forEach(e -> {
            e.tickCount++;
        });
    }
}
