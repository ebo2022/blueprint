package com.teamabnormals.blueprint.core.util.registry.forge;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
public class ItemSubRegistryHelperImpl {

    public static SpawnEggItem createSpawnEggItem(Supplier<EntityType<? extends Mob>> supplier, int primaryColor, int secondaryColor) {
        return new DeferredSpawnEggItem(supplier, primaryColor, secondaryColor, new Item.Properties());
    }
}
