package com.teamabnormals.blueprint.core.fabric;

import com.teamabnormals.blueprint.common.item.FuelItem;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

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
    // We can use this to mimic some Forge events knowing Blueprint-dependent mods already finished their registration
    static void onPostInitialize() {
        BuiltInRegistries.ITEM.stream().filter(FuelItem.class::isInstance).forEach(item -> FuelRegistry.INSTANCE.add(item, ((FuelItem) item).getBurnTime()));
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
