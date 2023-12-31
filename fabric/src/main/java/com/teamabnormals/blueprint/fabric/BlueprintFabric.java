package com.teamabnormals.blueprint.fabric;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.events.lifecycle.ModLifecycleEvents;
import com.teamabnormals.blueprint.core.util.IParallelDispatcher;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class BlueprintFabric implements ModInitializer, DedicatedServerModInitializer {

    static MinecraftServer server;

    @Override
    public void onInitialize() {
        Blueprint.init();
    }

    // Server initializers are ran after the regular ones meaning registries should be complete
    @Override
    public void onInitializeServer() {
        ModLifecycleEvents.COMMON_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.DEDICATED_SERVER_SETUP.getInvoker().event(IParallelDispatcher.EMPTY);
        ModLifecycleEvents.LOAD_COMPLETE.getInvoker().event(IParallelDispatcher.EMPTY);
    }

    @ApiStatus.Internal
    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}
