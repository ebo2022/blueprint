package com.teamabnormals.blueprint.core.fabric;

import com.teamabnormals.blueprint.common.block.LogBlock;
import com.teamabnormals.blueprint.common.item.BEWLRFuelBlockItem;
import com.teamabnormals.blueprint.common.item.FuelBlockItem;
import com.teamabnormals.blueprint.common.item.FuelItem;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The fabric initializer for the Blueprint mod.
 *
 * @author ebo2022
 */
public class BlueprintFabric implements ModInitializer, DedicatedServerModInitializer {

    static MinecraftServer server;

    @Override
    public void onInitialize() {
        Blueprint.init();
        BlueprintFabricEvents.register();
    }

    // Note: This works because ClientModInitializer & DedicatedServerModInitializer are run after regular ModInitializers
    // We use BlueprintModInitializer to enforce registration being complete for all Blueprint-dependent mods, before this runs
    static void onPostInitialize() {
        forType(BuiltInRegistries.BLOCK, LogBlock.class, block -> StrippableBlockRegistry.register(block, block.getBlock().get()));
        forType(BuiltInRegistries.ITEM, FuelItem.class, item -> FuelRegistry.INSTANCE.add(item, item.getBurnTime()));
        forType(BuiltInRegistries.ITEM, FuelBlockItem.class, item -> FuelRegistry.INSTANCE.add(item, item.getBurnTime()));
        forType(BuiltInRegistries.ITEM, BEWLRFuelBlockItem.class, item -> FuelRegistry.INSTANCE.add(item, item.getBurnTime()));
    }

    @SuppressWarnings("unchecked")
    static <T, R extends T> void forType(Registry<T> registry, Class<R> clazz, Consumer<R> consumer) {
        registry.stream().filter(clazz::isInstance).forEach(object -> consumer.accept((R) object));
    }

    @Override
    public void onInitializeServer() {
        ModLifecycleEvents.DEDICATED_SERVER_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.COMMON_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.LOAD_COMPLETE.getInvoker().event(IParallelDispatcher.EMPTY);
        onPostInitialize();
    }

    @ApiStatus.Internal
    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}
